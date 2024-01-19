
# DAI-PW4 - Drink Manager

A brief description of what this project does and who it's for


## Demo

Small CRUD API to manage drinks
```cmd
curl -i -X POST -H "Content-Type: application/json" -d "{\"name\":\"Sprite\", \"price\":\"3.60\"}" http://host.docker.internal:8080/users
```

To get all the drinks you can access 
```html
http://localhost:8080/drinks
```

To get drink by its id you can access
```html
http://localhost:8080/drinks/{id}
```