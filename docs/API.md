# Drink Manager API

The Drink Manager API allows to manage drinks. It uses the HTTP protocol on port `8080`.

The JSON format is used to exchange data. The `Content-Type` header must be set
to `application/json` when sending data to the API. The `Accept` header must be
set to `application/json` when receiving data from the API.

The API is based on the CRUD pattern. It has the following operations:

Auth
- Login
- Logout
- Create manager
- List manager
- Delete manager

Drinks
- Create a new drink
- Get all drinks
- Get one drink by its ID
- Update one drink
- Delete one drink

Tables
- Create a new table
- Delete a table
- Get all tables
- Select a table
- Unselect a table

Cart
- add drinks to cart
- remove drinks to cart
- get cart



## Endpoints

`Authentification`
======

Manager have rights to :
- create, delete, logout managers
- create, update, delete drinks
- create, delete tables

### Login
- `POST /login`

Login to have access to restricted endpoints

#### Request

The request body must contain a JSON object with the following properties:

- `username` - username of the user/manager
- `password` - password of the user/manager

#### Example query
```json
curl -i -X POST -H "Content-Type: application/json" -d 
'{
    "username":"root",
    "password":"1234"
}'http://localhost:8080/login

```

#### Response

The response body contains a text object `Authentication successful for user: {username}`

The response body contains a cookie `sessionToken`

#### Example cookie response
```
Set-Cookie: {sessionToken}; Path=/
```
with sessionToken example value:
```
Set-Cookie: sessionToken=a2c8b844-23f5-43ac-8a73-a5f5e088f479; Path=/
```

#### Example Response

#### Status codes

- `200` (OK) - login successful 
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - username or password invalid




### Logout
- `POST /logout` *restricted endpoints, see login*

Logout manager

#### Request

The request body must contain a valid `sessionToken` Cookie

#### Example query
```json
curl -i --cookie {sessionToken} -X POST http://localhost/logout
```
with sessionToken example value:
```json
curl -i --cookie sessionToken=a2c8b844-23f5-43ac-8a73-a5f5e088f479 -X POST http://localhost/logout
```

#### Response

The response body contains a text object : `Authentication successful for user: {username}`

The response body contains an expired `sessionToken` cookie

#### Example cookie response
```
Set-Cookie: sessionToken=; Path=/; Expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0
```

#### Status codes

- `204` (No Content) - logout successful 
- `401` (Unauthorized) - invalide sessionToken




### Create a new manager

- `POST /managers` *restricted endpoints, see login*

Create a new manager.

#### Request

The request body must contain a JSON object with the following properties:

- `username` - username of the new user/manager
- `password` - password of the new user/manager

The request body must contain a valid `sessionToken` Cookie

#### Example query
```json
curl -i --cookie {sessionToken} -X POST -H "Content-Type: application/json" -d 
'{
    "username":"toto",
    "password":"titi"
}'  http://localhost:8080/managers
```

#### Response

The response body contains a JSON object with the following properties:

- `username` - username of the new user/manager
- `password` - password of the new user/manager

#### Status codes

- `201` (Created) - The drink has been successfully created
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - invalide sessionToken
- `409` (Conflict) - The username already exists




### List all managers

- `GET /managers` *restricted endpoints, see login*

Get all managers username

#### Request

The request body must contain a valid `sessionToken` Cookie

#### Example query
```json
curl -i --cookie {sessionToken} http://localhost:8080/managers
```
#### Response

The response body contains a JSON array with the following properties:

- `username` - username of the user/manager

#### Status codes

- `200` (OK) - request successful 
- `401` (Unauthorized) - invalide sessionToken




### Delete manager

- `DELETE /managers/{username}` *restricted endpoints, see login*

Delete a manager.

/!\ It is not possible to delete your own user /!\

#### Request

The request path must contain the `username` of the manager to delete

The request body must contain a valid `sessionToken` Cookie

#### Example query
```json
curl -i --cookie {sessionToken} -X DELETE http://localhost:8080/managers/toto
```

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - delete successful
- `401` (Unauthorized) - invalide sessionToken
- `401` (Unauthorized) - deleting own user is not allowed
- `404` (Not Found) - The username does not exist





`Table`
======

### Create a new table

- `POST /tables` *restricted endpoints, see login*

Create a new table.

#### Request

The request body must contain a JSON object with the following properties:

- `id` - id of the new table
- `seats` - number of seats of the new table

The request body must contain a valid `sessionToken` Cookie

#### Example query
```json
curl -i --cookie {sessionToken} -X POST -H "Content-Type: application/json" -d 
'{
    "id":"11",
    "seats":"8"
}' http://localhost:8080/tables
```

#### Response

The response body contains a JSON object with the following properties:

- `id` - id of the table
- `seats` - number of seats of the new table
- `available` - status

#### Status codes

- `201` (Created) - The drink has been successfully created
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - invalide sessionToken
- `409` (Conflict) - Table id already exists




### Delete table

- `DELETE /tables/{id}` *restricted endpoints, see login*

Delete a table with its ID

#### Request

The request path must contain the `id` of the table to delete

The request body must contain a valid `sessionToken` Cookie

#### Example query
```json
curl -i --cookie {sessionToken} -X DELETE http://localhost:8080/tables/11
```

#### Response

The response body is empty.

#### Status codes

- `200` (OK) - request successful 
- `401` (Unauthorized) - invalide sessionToken
- `404` (Not Found) - table id does not exist




### Get all tables

- `GET /tables`

get all tables

#### Request

The request doesn't contain a parameter.

#### Example query
```json
curl -i  -X GET http://localhost:8080/tables
```

#### Response

The response body contains a JSON array with the following properties:

- `id` - id of the table
- `seats` - number of seats of the new table
- `available` - status

#### Status codes

- `200` (OK) - request successful




### Set a table

- `GET /setTable/{id}`

Select a table to be able to add drinks in cart

#### Request

The request path must contain the `id` of the table to select

#### Example query
```json
curl -i -X GET http://localhost:8080/setTable/10
```

#### Response

The response body contains a cookie `tableId`

#### Example cookie response
```
Set-Cookie: {tableId}; Path=/
```
with tableId example value:
```
Set-Cookie: tableId=11; Path=/
```

#### Status codes

- `200` (OK) - request successful
- `404` (Not Found) - table id does not exist




### unset a table

- `GET /clearTable`

Clear `tableId` cookie

#### Request

The request doesn't contain a parameter.

#### Example query
```json
curl -i -X GET http://localhost:8080/clearTable
```

#### Response

The response body contains an expired `tableId` cookie

#### Example cookie response
```
Set-Cookie: tableId=; Path=/; Expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0
```
#### Status codes

- `200` (OK) - request successful





`Drinks`
======

### Create a new drink

- `POST /drinks` *restricted endpoints, see login*

Create a new drinks.

#### Request

The request body must contain a JSON object with the following properties:

- `name` - name of the drink
- `price` - price of the drink

The request body must contain a valid sessionToken Cookie

#### Example query
```json
curl -i --cookie {sessionToken} -X POST -H "Content-Type: application/json" -d 
    "{
    \"name\":\"Sprite\", 
    \"price\":\"3.60\"
    }" 
    http://localhost:8080/drinks
```

#### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the drink
- `name` - The name of the drink
- `price` - The price of the drink

#### Status codes

- `201` (Created) - The drink has been successfully created
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - invalide sessionToken
- `409` (Conflict) - The drink already exists






### Get all drinks

- `GET /drinks`

Get all drinks.

#### Request

The request doesn't contain a parameter.

#### Example usage
To get a list of all the drinks you can access/call 
```html
http://localhost:8080/drinks
```

#### Response

The response body contains a JSON array with the following properties:

- `id` - The unique identifier of the drink
- `name` - The name of the drink
- `price` - The price of the drink

#### Status codes

- `200` (OK) - Drinks have been successfully retrieved






### Get one drink

- `GET /drinks/{id}`

Get one drink by its ID.

#### Request

The request path must contain the ID of the drink.

#### Example usage
To get a drink by its id you can access/call, where {id} is the id of the desired drink
```html
http://localhost:8080/drinks/{id}
```

#### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the drink
- `name` - The name of the drink
- `price` - The price of the drink

#### Status codes

- `200` (OK) - The drink has been successfully retrieved
- `404` (Not Found) - The drink does not exist






### Update a drink

- `PUT /drinks/{id}` *restricted endpoints, see login*

Update a drink by its ID.

#### Request

The request path must contain the ID of the drink.

The request body must contain a JSON object with the following properties:

- `name` - The name of the drink
- `price` - The price of the drink

The request body must contain a valid `sessionToken` Cookie

#### Example query
```json
curl -i --cookie {sessionToken} -X PUT -H "Content-Type: application/json" -d 
    "{
    \"name\":\"Coca\", 
    \"price\":\"3.60\"
    }" 
    http://localhost:8080/drinks/{id}
```

#### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the drink
- `name` - The name of the drink
- `price` - The price of the drink

#### Status codes

- `200` (OK) - The drink has been successfully updated
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - invalide sessionToken
- `404` (Not Found) - The drink does not exist





### Delete a drink

- `DELETE /drinks/{id}` *restricted endpoints, see login*

Delete a drink by its ID.

#### Request

The request path must contain the `ID` of the drink.

The request body must contain a valid `sessionToken` Cookie

#### Example query
To delete a drink by its id
```cmd
curl -i --cookie {sessionToken} -X DELETE http://localhost:8080/drinks/{id}
```

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The drink has been successfully deleted
- `401` (Unauthorized) - invalide sessionToken
- `404` (Not Found) - The drink does not exist




`Cart`
======

### Add a drink to cart

- `POST /cart` *restricted endpoints, see setTable*

Add a drink to the cart.
If the drink is already in the cart, the current quantity is increased by the quantity defined in the request.

#### Request

The request body must contain a JSON object with the following properties:

- `drinkId`  - id of the drink
- `quantity` - quantity of drink ordered

The request body must contain a `tableId` Cookie

#### Example query
```json
curl -i --cookie {tableId} -X POST -H "Content-Type: application/json" -d 
    "{
    \"drinkId\":\"2\", 
    \"quantity\":\"3\"
    }" 
    http://localhost:8080/cart
```

#### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the drink
- `name` - The name of the drink
- `price` - The price of the drink

#### Status codes

- `204` (No Content) - The drink has been successfully added to cart
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - invalide tableId
- `404` (Not Found) - The drink does not exist




### Remove a drink from cart

- `DELETE /cart/{id}` *restricted endpoints, see setTable*

Remove a drink from the cart entirely.

#### Request

The request path must contain the `ID` of the drink.

The request body must contain a `tableId` Cookie

#### Example query
```cmd
curl -i --cookie {tableId} -X DELETE http://localhost:8080/cart/{drinkId}
```

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The drink has been successfully removed from cart
- `401` (Unauthorized) - invalide tableId
- `404` (Not Found) - The drink does not exist




### Get cart list

- `GET /cart` *restricted endpoints, see setTable*

Get a list of all items in the cart.

#### Request

The request body must contain a `tableId` Cookie

#### Example usage
To get a list of all items in cart 
```cmd
curl -i --cookie {tableId} -X GET http://localhost:8080/cart
```

#### Response

The response body contains a JSON array with the following properties:

- `drink` - The drink
    - `id` - Id of the drink
    - `name` - The name of the drink
    - `price` - The price of the drink
- `quantity` - The quantity of the drink in the cart

##### Response example
```
{
  "drinks": [
    {
      "drink": {
        "id": 2,
        "name": "Rivella",
        "price": 2.35
      },
      "quantity": 3
    },
    {
      "drink": {
        "id": 1,
        "name": "Coca",
        "price": 2.6
      },
      "quantity": 2
    },
    {
      "drink": {
        "id": 3,
        "name": "Fanta",
        "price": 3.35
      },
      "quantity": 3
    }
  ]
}
```

#### Status codes

- `200` (OK) - Drinks have been successfully retrieved
- `401` (Unauthorized) - invalide tableId