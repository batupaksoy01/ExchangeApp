# Exchange Application 

A spring boot web application which users can access current exchange rates, save currency conversions to H2 database and get the previously saved conversions. 
There are three endpoints and all of them are covered below with the parameters they have and the responses they return.

## Response

Application will return one of the classes below depending on the type and success of the operation.

```
ConversionDTO
{
  "source": string,
  "target": string,
  "sourceAmount": double,
  "targetAmount": double
}

ExchangeRateDTO
{
  "source": string,
  "target": string,
  "rate": double
}

ErrortDTO
{
  "status": integer,
  "error: string,
  "message": string
}
```

## GET /api/exchange-rate

Description: Returns the exchange rate when converting from source currency to target currency.  
<br /> Return Type: `ExchangeRateDto`
<br /> Params:


|          Name | Required |  Type                                                                                                                                                               |
| -------------:|:--------:|:------- 
|     `source` | required | String                                                                        
|     `target` | required | String   

Example call and response:

```
http://localhost:8080/api/exchange-rate?source=EUR&target=GBP
```

```
{
    "source": "EUR",
    "target": "GBP",
    "rate": 0.850668
}
```

## POST /api/conversions
Description: Requires a ConversionInputDTO object with all the fields in the request body. Then the target amount is calculated with the exchange rate and the 
sent conversion is then saved to h2 in memory database. The saved conversion is returned.
<br /> Return Type: `ConversionDto`
<br /> Request Body:  
```
ConversionInputDTO
{
  "source": String,
  "target": String,
  "sourceAmount": double
}
```
Example call and response:

```
http://localhost:8080/api/conversions

//with

{
    "source": "CAD",
    "target": "TRY",
    "sourceAmount": 120
}
```

```
{
    "source": "CAD",
    "target": "TRY",
    "sourceAmount": 120.0,
    "targetAmount": 1452.87864
}
```

## GET /api/conversions
Returns the list of conversions that were saved to database by the POST mehod described above. Supports pagination.

|          Name | Required |  Type   | Default Value                                                                                                                                                            |
| -------------:|:--------:|:-------:|---------------- 
|     `page` | optional | integer     |    0(First Page)                                                               
|     `size` | optional | integer   |       10
|     `sort` | optional | Sort Object |  no sorting(default direction is ASC)

Example call and response:

```
http://localhost:8080/api/conversions?page=0&size=2&sort=targetAmount,DESC
```

```
[
    {
        "source": "EUR",
        "target": "TRY",
        "sourceAmount": 100.0,
        "targetAmount": 1632.0235
    },
    {
        "source": "CAD",
        "target": "TRY",
        "sourceAmount": 120.0,
        "targetAmount": 1453.50384
    }
]
```

## /swagger-ui.html and /v2/api-docs

Visit the urls to access the swagger generated documentations.
