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
`Drinks`
======

### Create a new drink

- `POST /drinks`

Create a new drinks.

#### Request

The request body must contain a JSON object with the following properties:

- `name` - name of the drink
- `price` - price of the drink

#### Example query
```json
curl -i -X POST -H "Content-Type: application/json" -d 
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

- `PUT /drinks/{id}`

Update a drink by its ID.

#### Request

The request path must contain the ID of the drink.

The request body must contain a JSON object with the following properties:

- `name` - The name of the drink
- `price` - The price of the drink

#### Example query
```json
curl -i -X PUT -H "Content-Type: application/json" -d 
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
- `404` (Not Found) - The drink does not exist

### Delete a drink

- `DELETE /drinks/{id}`

Delete a drink by its ID.

#### Request

The request path must contain the ID of the drink.

#### Example query
To delete a drink by its id
```cmd
curl -i -X DELETE http://localhost:8080/drinks/{id}
```

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The drink has been successfully deleted
- `404` (Not Found) - The drink does not exist

`Cart`
======

### Add a drink to cart

- `POST /cart`

Add a drink to the cart.
If the drink is already in the cart, the current quantity is increased by the quantity defined in the request.

#### Request

The request body must contain a JSON object with the following properties:

- `drinkId`  - id of the drink
- `quantity` - quantity of drink ordered

#### Example query
```json
curl -i -X POST -H "Content-Type: application/json" -d 
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
- `404` (Not Found) - The drink does not exist

### Remove a drink from cart

- `DELETE /cart/{id}`

Remove a drink from the cart entirely.

#### Request

The request path must contain the ID of the drink.

#### Example query
```cmd
curl -i -X DELETE http://localhost:8080/cart/{drinkId}
```

#### Response

The response body is empty.

#### Status codes

- `204` (No Content) - The drink has been successfully removed from cart
- `404` (Not Found) - The drink does not exist

### Get cart list

- `GET /cart`

Get a list of all items in the cart.

#### Request

The request doesn't contain a parameter.

#### Example usage
To get a list of all items in cart 
```html
http://localhost:8080/cart
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