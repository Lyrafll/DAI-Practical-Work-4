# Drink Manager API

The Drink Manager API allows to manage drinks. It uses the HTTP protocol on port `8080`.

The JSON format is used to exchange data. The `Content-Type` header must be set
to `application/json` when sending data to the API. The `Accept` header must be
set to `application/json` when receiving data from the API.

The API is based on the CRUD pattern. It has the following operations:

- Create a new drink
- Get all drinks
- Get one drink by its ID
- Update one drink
- Delete one drink

## Endpoints

### Create a new drink

- `POST /drinks`

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

- `PUT /drinks/{id}`

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

- `DELETE /drinks/{id}`

Delete a drink by its ID.

#### Request

The request path must contain the ID of the drink.

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The drink has been successfully deleted
- `404` (Not Found) - The drink does not exist
