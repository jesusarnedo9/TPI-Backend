Importing the `backend-tps` realm and using the Postman collection

Files created/updated:
- `ApiGateway/keycloak-realm-export.json` — full realm export for `backend-tps` (roles, clients, users)
- `Postman/Backend-TPS-collection.json` — Postman collection with token request and example endpoints

Steps to import the realm into Keycloak (UI):
1. Open Keycloak Admin Console: http://localhost:9090 (adjust host/port if different)
2. Click Realms -> Add realm -> Select the Import tab.
3. Upload `ApiGateway/keycloak-realm-export.json` and confirm. The realm `backend-tps` will be created with example clients and users.

Quick alternative: import using Admin REST API (example using curl):
1. Get admin token (master realm):

```powershell
$token = (curl -s -X POST "http://localhost:9090/realms/master/protocol/openid-connect/token" -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=password&client_id=admin-cli&username=admin&password=admin" | ConvertFrom-Json).access_token
```

2. POST the JSON to create the realm:

```powershell
curl -v -X POST "http://localhost:9090/admin/realms" -H "Authorization: Bearer $token" -H "Content-Type: application/json" --data-binary @ApiGateway/keycloak-realm-export.json -i
```

If the JSON import returns a parsing error, create minimal resources step-by-step (realm, roles, clients, users) using the Admin API as shown in the earlier session.

Using the Postman collection
1. Import `Postman/Backend-TPS-collection.json` into Postman (File -> Import).
2. Request a token using the request "01 - Get token (password grant)". It uses the `postman` client and `cliente1` user by default.
3. Copy the value of `access_token` from the token response and set it to the collection/environment variable `access_token` (or use Postman test script to store it automatically).
4. Use the other requests (Viajes, OSRM) replacing `{{TRAMO_ID}}` or other placeholders.

Notes
- Client `postman` has secret `postman-secret`.
- Example users/passwords in the realm:
  - admin / admin
  - cliente1 / cliente1
  - transportista1 / transportista1
  - testuser / password

OSRM examples
- Example OSRM route request:
  `GET http://localhost:5000/route/v1/driving/{lon1},{lat1};{lon2},{lat2}?overview=false&annotations=duration,distance`

If you want, I can also:
- Add Postman environment variables and a small test script to auto-store the token into `access_token`.
- Add more exact requests (full body schemas) once you point to the exact controller paths for Solicitudes/Tarifas (I included placeholders already).

