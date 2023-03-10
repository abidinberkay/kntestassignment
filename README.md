<h1>Kuehne-Nagel test assignment</h1>
<h2>Outline</h2>
<h2>1-Prerequisites and Running</h2>
<h3>1.1 Running with docker & docker-compose (recommended for almost zero effort)<h3>
<h3>1.2 Running without docker</h3>
<h2>2-Testing</h2>
<h2>3-Extra Notes</h2>

---
<h3>1-Prerequisites</h3>
Firstly be sure that no application is running on ports :`3307`, `8080` and `3000`.

**MySQL database** will work on: `3307`

**Backend app** will work on: `8080`

**Frontend app** will work on: `3000`

Install Docker & Docker-Compose (If you have already skip this step, go to 1.1):

https://docs.docker.com/get-docker/

https://docs.docker.com/compose/install/

<h3>1.1 Running with docker & docker-compose (recommended for almost zero effort)</h3>

**1-** When you install docker & docker-compose, go to project main folder where the docker-compose.yml is located.

**2-** Run the command ```docker-compose up -d```.

**3-** You should wait around 2-3 minutes so that the projects will install the required dependencies
and build the project and then will be ready to run.

**4-** When the projects are ready (3 containers: `knmysql`, `knbackend` and `knfrontend` are working correctly)
You can go to `localhost:3000`. Here is the login page. You can login or register new user to sign in.
(knmysql takes abit time to be ready so that knbackend will fail couple of times due to waiting for knmysql setup, all of them will be ready in 2-3 minutes depending on internet, computer etc. connection speed.)

**5-** You can see swagger/open-api UI to test backend with requests without frontend ui on
http://localhost:8080/docs/swagger-ui/index.html

<h3>1.2 Running without docker</h3>

You can download and install MySQL and create db with following credentials:
`jdbcUrl=jdbc:mysql://localhost:3307/knmysql
username=kntestuser
password=kntestpass`

Run the backend project from local (through IDE or `java -jar knbackend.jar` after building backend project with `mvn package`)

Run the frontend project from local (through IDE or `npm start` after building frontend project with `npm install`)

---

<h2>2-Testing </h2>

You can test the app through frontend (`localhost:3000`) or you can test the backend via swagger/open-api ui on
http://localhost:8080/docs/swagger-ui/index.html

In order to be able to see pages you need to login first (or register if you are not logged in).
For swagger-ui testers: You need to login and get the jwtToken response first (by calling "/authenticate" endpoint) 
and then paste the received jwtToken to the `Authorization` field on top right corner as follows:
![img.png](img.png)

---
<h2>3-Extra Notes</h2>
System has 2 users by default:

**1-** Username: **EDITOR** Password: **123456qwe** Role: **ROLE_ALLOW_EDIT**

**2-** Username: **READER** Password: **123456qwe** Role: **ROLE_DISALLOW_EDIT**

You can edit name or photo links of cities only if you have **ROLE_ALLOW_EDIT** role.

For registering new user, password complexity policy is:
```
at least 4 characters
at least one upper-case character
at least one digit character
no whitespace
```
If you want to change that behaviour you can edit the
`knbackend/src/main/java/com/kntest/knbackend/util/PasswordConstraintValidator.java` file 
and comment/uncomment desired behaviours.

"Sorting" was not included in the task, but it works on backend due to 
default behaviour of "Pageable" object. Sample request is:
![img_1.png](img_1.png)
  
For updating city, `@PreAuthorized` annotation is used for method level authorization (i.e. `@EnableGlobalMethodSecurity`). Instead of that, this can also be used that we can specify it explicitly on `SecurityConfiguration.java` class on backend, here is the corresponding method:
```  
  @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/auth/**", "/docs/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors();

    }
```
  
We can add here like `.antMatchers("/city")` and `hasAuthority("ROLE_ALLOW_EDIT")` **or** `hasRole("ALLOW_EDIT")` to enable these authorities to be able reach the endpoint.
