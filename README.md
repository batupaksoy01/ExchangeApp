# Exchange Application 

A spring boot web application which users can access current exchange rates, save currency conversions to H2 database and get the previously saved conversions. 
There are three endpoints and all of them are covered below with the parameters they have and the responses they return.

## Response

Application will return one of the classes below depending on the success of the operation.

```java
SuccessResponseDTO 
{
  private List<ConversionDTO> conversionList;
  
  public boolean isSuccess() {
	return true;
  }
}

FailResponseDTO 
{
  private String errorMessage;
  
  public boolean isSuccess() {
	return false;
  }
}

//Not a response by itself but SuccessResponseDTO has dependency to it
ConversionDTO 
{
  private String source;
  private String target;
  private float sourceAmount;
  private float targetAmount;
  private LocalDateTime dateTime;
}
```

## GET /api/exchangeRate

Returns the exchange rate when converting from source currency to target currency. The SuccessResponseDTO will only have one ConversionDTO in the list. 
That element's targetAmount field will contain the requested rate. The sourceAmount of that element will equal to 1 since the response returns the unit conversion rate.

|          Name | Required |  Type                                                                                                                                                               |
| -------------:|:--------:|:------- 
|     `source` | required | String                                                                        
|     `target` | required | String   

Example call and response:

```
http://localhost:8080/api/exchangeRate?source=EUR&target=GBP
```

```
{
"conversionList":[{
  "source":"EUR",
  "target":"GBP",
  "sourceAmount":1.0,
  "targetAmount":0.855162,
  "dateTime":"2022-05-09T14:36:17.5021989"
  }],
  "success":true
}
```

## POST /api/getConversion
Requires a ConversionInputDTO object with all the fields in the request body. Then the target amount is calculated with the exchange rate and the 
sent conversion is then saved to h2 in memory database. The SuccessResponseDTO will only have one ConversionDTO in the list and that will contain the information 
for the new database entry that was created by this POST request.

```
ConversionInputDTO
{
  "sourceAmount": float,
  "source": String,
  "target": String
}
```
Example call and response:

```
http://localhost:8080/api/getConversion

//with

{
  "sourceAmount": 25,
  "source": "USD",
  "target": "TRY"
}
```

```
{
    "conversionList": [
        {
            "source": "USD",
            "target": "TRY",
            "sourceAmount": 25.0,
            "targetAmount": 375.7373,
            "dateTime": "2022-05-09T14:54:06.8581612"
        }
    ],
    "success": true
}
```

## GET /api/getConversions
Returns the list of conversions that were saved to database by the POST mehod described above. Supports pagination.

|          Name | Required |  Type   | Default Value                                                                                                                                                            |
| -------------:|:--------:|:-------:|---------------- 
|     `page` | optional | integer     |    1(First Page)                                                               
|     `size` | optional | integer   |       10

Example call and response:

```
http://localhost:8080/api/getConversions?page=1&size=2
```

```
{
"conversionList":[{
  "source":"EUR",
  "target":"GBP",
  "sourceAmount":10,
  "targetAmount":8.55162,
  "dateTime":"2022-05-09T14:36:17.5021989"
  },
  "source":"USD",
  "target":"GBP",
  "sourceAmount":100,
  "targetAmount":80.995923,
  "dateTime":"2022-05-09T14:42:37.013544"
  }],
  "success":true
}
```

## /swagger-ui.html and /v2/api-docs

Visit the urls to access the swagger generated documentations.
