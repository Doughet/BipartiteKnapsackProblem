package org.example.project.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.xhr.BLOB
import org.w3c.xhr.FormData
import org.w3c.xhr.XMLHttpRequest
import org.w3c.xhr.XMLHttpRequestResponseType

class AnticipationViewModel {
    val sheetName : MutableState<String> = mutableStateOf("")
    val NLimit : MutableState<Float> = mutableStateOf(0.0f)
    val PLimit : MutableState<Float> = mutableStateOf(0.0f)

    val C1 : MutableState<Int> = mutableStateOf(0)
    val C2 : MutableState<Int> = mutableStateOf(0)

    val fileAnticipation : MutableState<ByteArray?> = mutableStateOf(null)

    var isLoaded : Boolean = false
    val jsArray : MutableState<JsArray<JsAny?>> = mutableStateOf(JsArray<JsAny?>())

    fun setSheetName(text : String){
        sheetName.value = text
    }

    fun getSheetName() : String{
        return sheetName.value
    }

    fun getNLimit() : Float{
        return NLimit.value
    }

    fun setNLimit(nLimit : Float){
        NLimit.value = nLimit
    }

    fun getPLimit() : Float{
        return PLimit.value
    }

    fun setPLimit(pLimit : Float){
        PLimit.value = pLimit
    }


    fun getC1() : Int{
        return C1.value
    }

    fun setC1(C1 : Int){
        this.C1.value = C1
    }

    fun getC2() : Int{
        return C2.value
    }

    fun setC2(C2 : Int){
        this.C2.value = C2
    }

    fun pickFile(onFilePicked: (ByteArray, String) -> Unit){
        // Create an HTML input element dynamically
        val inputElement = document.createElement("input") as HTMLInputElement
        inputElement.type = "file"
        inputElement.accept = ".xlsx"  // Accept only Excel files

        // Add an event listener to handle the file selection
        inputElement.onchange = {
            val file: File? = inputElement.files?.item(0)
            file?.let {
                val reader = FileReader()
                reader.onload = {
                    val arrayBuffer = reader.result as? ArrayBuffer
                    if (arrayBuffer != null) {
                        // Convert ArrayBuffer to Int8Array
                        val int8Array = Int8Array(arrayBuffer)
                        // Convert Int8Array to ByteArray
                        val byteArray = ByteArray(int8Array.length) { index -> int8Array[index] }
                        // Call the callback with the ByteArray and file name
                        //onFilePicked(byteArray, file.name)
                        fileAnticipation.value = byteArray
                        postFileUpload()
                        postAnticipation()
                    }
                }
                reader.readAsArrayBuffer(file)
            }
        }

        // Programmatically click the input element to open the file picker dialog
        inputElement.click()
    }

    fun postFileUpload(){
        val buffer = ArrayBuffer(fileAnticipation.value!!.size)
        val view = Int8Array(buffer)

        // Populate the ArrayBuffer with the ByteArray data
        fileAnticipation.value!!.forEachIndexed { index, byte ->
            view[index] = byte
        }

        jsArray.value.set(0, buffer)

        isLoaded = true
    }

    fun postAnticipation() : Boolean{
        if(!isLoaded){
            return false
        }

        val xhr = XMLHttpRequest()

        // Create a Blob from the Uint8Array
        val blob = Blob(jsArray.value, BlobPropertyBag("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))

        val formData = FormData()
        formData.append("file", blob)
        formData.append("sheetName", sheetName.value)
        formData.append("NLimit", NLimit.value.toString())
        formData.append("PLimit", PLimit.value.toString())
        formData.append("C1", C1.value.toString())
        formData.append("C2", C2.value.toString())

        xhr.open("POST", "http://localhost:8080/api/anticipation/upload")
        xhr.setRequestHeader("Accept", "text/plain") // Expecting a plain text response

        xhr.onload = {
            if (xhr.status == 200.toShort()) {
                downloadExcelFile()
            } else {

            }
        }

        xhr.send(formData)

        return true
    }

    fun downloadExcelFile() {
        val xhr = XMLHttpRequest()

        xhr.open("GET", "http://localhost:8080/api/anticipation/download", true)
        xhr.responseType = XMLHttpRequestResponseType.BLOB

        xhr.onload = {
            if (xhr.status == 200.toShort()) {
                val blob = xhr.response as Blob
                val url = URL.createObjectURL(blob)

                // Create a link element and trigger a download
                val link = document.createElement("a") as HTMLAnchorElement
                link.href = url
                link.download = "truck_anticipation.xlsx"
                document.body?.appendChild(link)
                link.click()
                document.body?.removeChild(link)
                URL.revokeObjectURL(url)
            } else {
            }
        }

        xhr.onerror = {
        }

        xhr.send()
    }

}