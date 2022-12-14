package pt.ulusofona.deisi.cm2122.g21700980_21906966.management

import retrofit2.http.GET
import retrofit2.http.Query

//data class PostOperationRequest(val expression: String, val result: Double, val timestamp: Long)
//data class PostOperationResponse(val message: String)

data class ResponseData(
    val id: String,

    val district: String,
    val concelho: String,
    val freguesia: String,
    val location : String,

    val status: String,

    val lat: Double,
    val lng: Double,

    val date : String,
    val hour : String,
    val man : Int,

    val created : HashMap<String, Long>,
    val sadoId : String,

)

data class GetFiresResponse(
    val data : List<ResponseData>
)

data class GetRisk(
    val data : String
)

//data class DeleteOperationByIdResponse(val message: String)

interface FireService {

//    @GET("operations")
//    suspend fun getOne(@Body body: PostOperationRequest): PostOperationResponse

    @GET("new/fires")
    suspend fun getAll(): GetFiresResponse

    @GET("v1/risk?")
    suspend fun getRisk(@Query("concelho") district: String): GetRisk
}

/*

 {
      "_id": {
        "$id": "629b408e82eaf209da1fc4db"
      },
      "id": "2022090020205",
      "coords": true,
      "dateTime": {
        "sec": 1654339800
      },
      "date": "04-06-2022",
      "hour": "11:50",
      "location": "Guarda, Guarda, Panoias De Cima",
      "aerial": 0,
      "man": 11,
      "terrain": 3,
      "district": "Guarda",
      "concelho": "Guarda",
      "freguesia": "Panoias De Cima",
      "dico": "0907",
      "lat": 40.511735,
      "lng": -7.25304,
      "naturezaCode": "3103",
      "natureza": "Mato",
      "especieName": "Incêndios Rurais",
      "familiaName": "Riscos Mistos",
      "statusCode": 9,
      "statusColor": "65C4ED",
      "status": "Vigilância",
      "important": false,
      "localidade": "Póvoa De S. Domingos",
      "active": true,
      "sadoId": "2022090020205",
      "sharepointId": 147208743,
      "extra": null,
      "disappear": false,
      "icnf": {
        "altitude": 845.781,
        "fogacho": true,
        "fontealerta": "112"
      },
      "detailLocation": "Póvoa de S. Domingos (EN233 (EN233) Km 2)",
      "kml": null,
      "created": {
        "sec": 1654341774
      },
      "updated": {
        "sec": 1654354084
      }
    },
 */