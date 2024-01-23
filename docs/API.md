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



## Endpoints Authentification
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

#### Response

The response body contains a text object `Authentication successful for user: {username}`

The response body contains a cookie `sessionToken`

#### Status codes

- `200` (OK) - login successful 
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - username or password invalid




### Logout
- `POST /logout` *restricted endpoints, see login*

Logout manager

#### Request

The request body must contain a valid `sessionToken` Cookie

#### Response

The response body contains a text object : `Authentication successful for user: {username}`

The response body contains an expired `sessionToken` cookie

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

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - delete successful
- `401` (Unauthorized) - invalide sessionToken
- `401` (Unauthorized) - deleting own user is not allowed
- `404` (Not Found) - The username does not exist





## Endpoints Tables

### Create a new table

- `POST /tables` *restricted endpoints, see login*

Create a new table.

#### Request

The request body must contain a JSON object with the following properties:

- `id` - id of the new table
- `seats` - number of seats of the new table

The request body must contain a valid `sessionToken` Cookie

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

#### Response

The response body contains a cookie `tableId`

#### Status codes

- `200` (OK) - request successful
- `404` (Not Found) - table id does not exist




### unset a table

- `GET /clearTable`

Clear `tableId` cookie

#### Request

The request doesn't contain a parameter.

#### Response

The response body contains an expired `tableId` cookie

#### Status codes

- `200` (OK) - request successful






## Endpoints Cart
//TODO




## Endpoints Drinks

### Create a new drink

- `POST /drinks` *restricted endpoints, see login*

Create a new drinks.

#### Request

The request body must contain a JSON object with the following properties:

- `name` - name of the drink
- `price` - price of the drink

#### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the drink
- `name` - The name of the drink
- `price` - The price of the drink

#### Status codes

- `201` (Created) - The drink has been successfully created
- `400` (Bad Request) - The request body is invalid
- `409` (Conflict) - The drink already exists




### Get all drinks

- `GET /drinks`

Get all drinks.

#### Request

The request doesn't contain a parameter.

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

#### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the drink
- `name` - The name of the drink
- `price` - The price of the drink

#### Status codes

- `200` (OK) - The drink has been successfully updated
- `400` (Bad Request) - The request body is invalid
- `404` (Not Found) - The drink does not exist




### Delete a drink

- `DELETE /drinks/{id}` *restricted endpoints, see login*

Delete a drink by its ID.

#### Request

The request path must contain the ID of the drink.

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The drink has been successfully deleted
- `404` (Not Found) - The drink does not exist
