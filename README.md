
# DAI-PW4 - Drink Manager

A brief description of what this project does and who it's for


## Demo

Small CRUD API to manage drinks
```cmd
curl -i -X POST -H "Content-Type: application/json" -d "{\"name\":\"Sprite\", \"price\":\"3.60\"}" http://localhost:8080/drinks
```

To get all the drinks you can access 
```html
http://localhost:8080/drinks
```

To get drink by its id you can access
```html
http://localhost:8080/drinks/{id}
```

To update a drink by its id
```cmd
curl -i -X PUT -H "Content-Type: application/json" -d "{\"name\":\"Coca
\", \"price\":\"3.60\"}" http://localhost:8080/drinks/{id}
```

To delete a drink by its id
```cmd
curl -i -X DELETE http://localhost:8080/drinks/{id}
```