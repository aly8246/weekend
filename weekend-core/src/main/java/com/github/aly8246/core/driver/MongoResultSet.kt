package com.github.aly8246.core.driver

import com.github.aly8246.core.annotation.BaseMethod
import com.github.aly8246.core.annotation.Mapping
import com.github.aly8246.core.annotation.WeekendId
import com.github.aly8246.core.configuration.Configurations.Companion.configuration
import com.github.aly8246.core.exception.WeekendException
import com.github.aly8246.core.dispatcher.baseDaoHandler.CollectionEntityResolver
import com.github.aly8246.core.util.PrintImpl
import com.mongodb.client.MongoCursor
import org.bson.Document
import java.io.InputStream
import java.io.Reader
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class MongoResultSet() : ResultSet {
    var resultRows: Int? = null

    constructor(resultRows: Int) : this() {
        this.resultRows = resultRows
    }

    private lateinit var query: MongoCursor<Document>
    private lateinit var mongoConnection: MongoConnection
    private lateinit var mongoStatement: MongoStatement

    constructor(query: MongoCursor<Document>, mongoConnection: MongoConnection, mongoStatement: MongoStatement) : this() {
        this.query = query
        this.mongoConnection = mongoConnection
        this.mongoStatement = mongoStatement
    }

    private lateinit var method: Method
    private var args: kotlin.Array<Any>? = null
    private var resultClassType: Class<*>? = null
    private var mappingMap: MutableMap<String, MongoMapping> = mutableMapOf()
    private var _id: Any? = null


    fun init(method: Method, args: kotlin.Array<Any>?, target: Class<*>) {
        this.method = method
        this.args = args
        var returnType = method.returnType


        val baseMethod = method.getDeclaredAnnotation(BaseMethod::class.java)

        //是基础方法
        if (baseMethod != null) {
            resultClassType = CollectionEntityResolver().returnClass(target)
        } else {
            val canonicalName = returnType.canonicalName
            when {
                canonicalName != "void" -> resultClassType = try {
                    //如果是集合则会创建实例失败
                    Class.forName(canonicalName).newInstance().javaClass
                } catch (e: InstantiationException) {
                    //取集合中的泛型的实际类型为返回值
                    Class.forName(regxListParamClass(method.toGenericString())).newInstance().javaClass
                }
            }
        }

        val result = this.resolverResult()
        this.mappingMap = result!!
    }

    private fun resolverResult(): MutableMap<String, MongoMapping>? {

        if (this.resultClassType == null) return null
        val declaredFields = resultClassType?.declaredFields

        val mappingList: MutableMap<String, MongoMapping> = mutableMapOf()
        declaredFields?.forEach { e ->
            run {
                val annotations = e.annotations
                annotations.forEach { annotation ->
                    run {
                        when (annotation) {
                            is Mapping -> {
                                val mongoMapping = MongoMapping()
                                var fieldName: String = annotation.name.toList().toString()
                                fieldName = fieldName.replace("[", "").replace("]", "")
                                mongoMapping.codeFieldName = e.name
                                mongoMapping.codeFieldType = e.type
                                mongoMapping.dbFieldName = fieldName
                                mongoMapping.dbFieldType = annotation.type.java
                                mappingList[mongoMapping.dbFieldName] = mongoMapping
                            }
                            is WeekendId -> {
                                _id = e.toString().split(".")[e.toString().split(".").count() - 1]
                            }
                            else -> {
                                val mongoMapping = MongoMapping()
                                mongoMapping.codeFieldName = e.name
                                mongoMapping.codeFieldType = e.type
                                mongoMapping.dbFieldName = e.name
                                mongoMapping.dbFieldType = e.type
                                mappingList[mongoMapping.dbFieldName] = mongoMapping
                            }
                        }
                    }
                }
            }
        }
        return mappingList
    }


    /**
     * If you want to query a List like 'List<User>'
     */
    private fun isList(): Boolean {
        val returnType = this.method.returnType
        val javaClass = this.resultClassType

        //如果是object则证明是BaseDao的接口返回的泛型参数
        val baseMethod = method.getDeclaredAnnotation(BaseMethod::class.java)
        if (returnType.name == "java.lang.Object" && baseMethod != null) return false

        return returnType != javaClass
    }

    /**
     * If you not need result
     */
    private fun isVoid(): Boolean {
        return resultClassType == null
    }

    private fun regxListParamClass(source: String): String {
        val matcher = Pattern.compile("(?<=java.util.List<).*?(?=>)").matcher(source)
        while (matcher.find()) return matcher.group()
        throw WeekendException("异常regxListParamClass:$source")
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * return all select
     */
    fun getObject(): Any? {

        val list: MutableList<Document> = mutableListOf()

        while (query.hasNext()) list.add(query.next())
        if (list.size == 0) return null

        if (configuration.showResult!!)
            list.forEach { e ->
                run {
                    PrintImpl().debug("queryResult >>   $e")
                }
            }

        return when {
            this.isList() -> mapToList(list, resultClassType)
            this.isVoid() -> null
            else -> {
                val resultList = mapToList(list, resultClassType)
                if (resultList.size > 1) throw WeekendException("That's too much result,find ${list.size} result in >>  $method")
                resultList[0]
            }
        }
    }

    private fun mapToList(resourceMap: List<Map<*, *>>?, clazz: Class<*>?): List<*> {
        val returnList: MutableList<Any> = ArrayList()
        if (resourceMap == null || resourceMap.isEmpty()) {
            return returnList
        }
        for (map in resourceMap) {
            val mapToPojo = this.mapToPojo(map, clazz)
            returnList.add(mapToPojo!!)
        }
        return returnList
    }

    private fun mapToPojo(map: Map<*, *>?, clazz: Class<*>?): Any? {
        if (map == null) return null
        var mappingMap: MutableMap<Any?, Any?> = map.toMutableMap()
        if (clazz == null) throw WeekendException("未知错误")
        val newInstance = clazz.newInstance()

        if (this._id != null) {
            mappingMap[this._id.toString()] = mappingMap["_id"]
        }
        for (key in mappingMap.keys) {
            var field: Field?
            field = try {
                //从newInstance中获取字段，尝试从map中用自己的字段名字来拿
                clazz.getDeclaredField(key as String)
            } catch (e: NoSuchFieldException) {
                try {
                    //尝试从映射拿mapping
                    val mapping = this.mappingMap[key.toString()]
                    clazz.getDeclaredField(mapping?.codeFieldName)
                } catch (e2: NullPointerException) {
                    try {
                        //尝试拿隐藏字段
                        clazz.getDeclaredField(key.toString().substring(1, key.toString().length))
                    } catch (e2: NoSuchFieldException) {
                        try {
                            clazz.getDeclaredField(this._id.toString())
                        } catch (e3: NoSuchFieldException) {
                            if (configuration.nonFieldRemind!!)
                                PrintImpl().debug("不存在的字段${key.toString()}")
                            continue
                        }
                    }
                }
            }

            field.isAccessible = true
            try {
                when {
                    field.type.name == "java.lang.String" -> {
                        field.set(newInstance, mappingMap[key].toString())
                    }
                    field.type.name == "java.lang.Integer" -> {
                        field.set(newInstance, mappingMap[key].toString().toInt())
                    }
                    field.type.name == "java.lang.Double" -> {
                        field.set(newInstance, mappingMap[key].toString().toDouble())
                    }
                    field.type.name == "java.lang.Long" -> {
                        field.set(newInstance, mappingMap[key].toString().toLong())
                    }
                    field.type.name == "java.lang.Short" -> {
                        field.set(newInstance, mappingMap[key].toString().toShort())
                    }
                    field.type.name == "java.lang.Float" -> {
                        field.set(newInstance, mappingMap[key].toString().toFloat())
                    }
                    field.type.name == "java.util.Date" -> {
                        try {
                            field.set(newInstance, SimpleDateFormat(configuration.dataFormat!!).parse(mappingMap[key].toString()))
                        } catch (e: Exception) {
                            field.set(newInstance, null)
                        }
                    }
                    field.type.name == "int" -> {
                        field.set(newInstance, mappingMap[key].toString().toInt())
                    }
                    field.type.name == "double" -> {
                        field.set(newInstance, mappingMap[key].toString().toDouble())
                    }
                    field.type.name == "long" -> {
                        field.set(newInstance, mappingMap[key].toString().toLong())
                    }
                    field.type.name == "short" -> {
                        field.set(newInstance, mappingMap[key].toString().toShort())
                    }
                    field.type.name == "float" -> {
                        field.set(newInstance, mappingMap[key].toString().toFloat())
                    }
                    else -> field.set(newInstance, mappingMap[key])
                }
            } catch (e: NumberFormatException) {
                field.set(newInstance, null)
            }
        }
        return newInstance
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean = false


    override fun findColumn(columnLabel: String?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNClob(columnIndex: Int): NClob {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNClob(columnLabel: String?): NClob {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNString(columnIndex: Int, nString: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNString(columnLabel: String?, nString: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStatement(): Statement {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTimestamp(columnIndex: Int, x: Timestamp?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTimestamp(columnLabel: String?, x: Timestamp?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNCharacterStream(columnIndex: Int, x: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNCharacterStream(columnIndex: Int, x: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNCharacterStream(columnLabel: String?, reader: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateInt(columnIndex: Int, x: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateInt(columnLabel: String?, x: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun moveToInsertRow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDate(columnIndex: Int): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDate(columnLabel: String?): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDate(columnIndex: Int, cal: Calendar?): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDate(columnLabel: String?, cal: Calendar?): Date {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWarnings(): SQLWarning {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeFirst() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
    }

    override fun updateFloat(columnIndex: Int, x: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateFloat(columnLabel: String?, x: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(columnIndex: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBoolean(columnLabel: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFirst(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBigDecimal(columnIndex: Int, scale: Int): BigDecimal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBigDecimal(columnLabel: String?, scale: Int): BigDecimal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBigDecimal(columnIndex: Int): BigDecimal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBigDecimal(columnLabel: String?): BigDecimal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBytes(columnIndex: Int, x: ByteArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBytes(columnLabel: String?, x: ByteArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLast(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertRow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTime(columnIndex: Int): Time {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTime(columnLabel: String?): Time {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTime(columnIndex: Int, cal: Calendar?): Time {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTime(columnLabel: String?, cal: Calendar?): Time {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rowDeleted(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun last(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isAfterLast(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun relative(rows: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun absolute(row: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSQLXML(columnIndex: Int): SQLXML {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSQLXML(columnLabel: String?): SQLXML {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun next(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFloat(columnIndex: Int): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFloat(columnLabel: String?): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wasNull(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRow(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun first(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getURL(columnIndex: Int): URL {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getURL(columnLabel: String?): URL {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateShort(columnIndex: Int, x: Short) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateShort(columnLabel: String?, x: Short) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNClob(columnIndex: Int, nClob: NClob?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNClob(columnLabel: String?, nClob: NClob?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNClob(columnIndex: Int, reader: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNClob(columnLabel: String?, reader: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNClob(columnIndex: Int, reader: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNClob(columnLabel: String?, reader: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRef(columnIndex: Int, x: Ref?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRef(columnLabel: String?, x: Ref?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateObject(columnIndex: Int, x: Any?, scaleOrLength: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateObject(columnIndex: Int, x: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateObject(columnLabel: String?, x: Any?, scaleOrLength: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateObject(columnLabel: String?, x: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun setFetchSize(rows: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterLast() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateLong(columnIndex: Int, x: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateLong(columnLabel: String?, x: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlob(columnIndex: Int): Blob {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBlob(columnLabel: String?): Blob {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClob(columnIndex: Int, x: Clob?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClob(columnLabel: String?, x: Clob?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClob(columnIndex: Int, reader: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClob(columnLabel: String?, reader: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClob(columnIndex: Int, reader: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateClob(columnLabel: String?, reader: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getByte(columnIndex: Int): Byte {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getByte(columnLabel: String?): Byte {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(columnIndex: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getString(columnLabel: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSQLXML(columnIndex: Int, xmlObject: SQLXML?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSQLXML(columnLabel: String?, xmlObject: SQLXML?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDate(columnIndex: Int, x: Date?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDate(columnLabel: String?, x: Date?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHoldability(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getObject(columnIndex: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getObject(columnLabel: String?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getObject(columnIndex: Int, map: MutableMap<String, Class<*>>?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getObject(columnLabel: String?, map: MutableMap<String, Class<*>>?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> getObject(columnIndex: Int, type: Class<T>?): T {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Any?> getObject(columnLabel: String?, type: Class<T>?): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun previous(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDouble(columnIndex: Int, x: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDouble(columnLabel: String?, x: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLong(columnIndex: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLong(columnLabel: String?): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClob(columnIndex: Int): Clob {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClob(columnLabel: String?): Clob {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlob(columnIndex: Int, x: Blob?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlob(columnLabel: String?, x: Blob?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlob(columnIndex: Int, inputStream: InputStream?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlob(columnLabel: String?, inputStream: InputStream?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlob(columnIndex: Int, inputStream: InputStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBlob(columnLabel: String?, inputStream: InputStream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateByte(columnIndex: Int, x: Byte) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateByte(columnLabel: String?, x: Byte) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isClosed(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNString(columnIndex: Int): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNString(columnLabel: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCursorName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getArray(columnIndex: Int): Array {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getArray(columnLabel: String?): Array {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancelRowUpdates() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateString(columnIndex: Int, x: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateString(columnLabel: String?, x: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setFetchDirection(direction: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFetchSize(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCharacterStream(columnIndex: Int): Reader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCharacterStream(columnLabel: String?): Reader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isBeforeFirst(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBoolean(columnIndex: Int, x: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBoolean(columnLabel: String?, x: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshRow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rowUpdated(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBigDecimal(columnIndex: Int, x: BigDecimal?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateBigDecimal(columnLabel: String?, x: BigDecimal?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShort(columnIndex: Int): Short {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShort(columnLabel: String?): Short {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAsciiStream(columnIndex: Int): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAsciiStream(columnLabel: String?): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTime(columnIndex: Int, x: Time?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTime(columnLabel: String?, x: Time?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTimestamp(columnIndex: Int): Timestamp {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTimestamp(columnLabel: String?): Timestamp {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTimestamp(columnIndex: Int, cal: Calendar?): Timestamp {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTimestamp(columnLabel: String?, cal: Calendar?): Timestamp {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRef(columnIndex: Int): Ref {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRef(columnLabel: String?): Ref {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun moveToCurrentRow() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getConcurrency(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRowId(columnIndex: Int, x: RowId?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRowId(columnLabel: String?, x: RowId?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNCharacterStream(columnIndex: Int): Reader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNCharacterStream(columnLabel: String?): Reader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateArray(columnIndex: Int, x: Array?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateArray(columnLabel: String?, x: Array?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBytes(columnIndex: Int): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBytes(columnLabel: String?): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDouble(columnIndex: Int): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDouble(columnLabel: String?): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUnicodeStream(columnIndex: Int): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUnicodeStream(columnLabel: String?): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rowInserted(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(columnIndex: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInt(columnLabel: String?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNull(columnIndex: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNull(columnLabel: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRowId(columnIndex: Int): RowId {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRowId(columnLabel: String?): RowId {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearWarnings() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMetaData(): ResultSetMetaData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBinaryStream(columnIndex: Int): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBinaryStream(columnLabel: String?): InputStream {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFetchDirection(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}