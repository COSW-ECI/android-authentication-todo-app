# Android Authentication Todo App
Create an Android App that authenticates with a Spring Boot server. 

The application should store the token given by the server, and use it to access the Todo Api to create a simple application that displays and creates Todos.


#### Part 1: Android authentication

1) Download the given project and make sure it works correctly. Try the following command
to make sure that the server returns a token:

    ```` Bash
    curl -H "Content-Type: application/json" -X POST -d '{"username":"test@mail.com","password":"password"}' http://localhost:8080/user/login
    
    ````

2) Create a sample Android project and create a login form that validates the user inputs and 
uses input type password properly.


3) Add the Retrofit library to your Android Project 

     ```groovy
       compile 'com.squareup.retrofit2:retrofit:2.3.0'
       compile 'com.squareup.retrofit2:converter-gson:2.3.0'
       ``` 
   
4) Create an interface called NetworkService with the following code:

    ``` java
         interface NetworkService
            {
            
                  @POST( "users/login" )
                  Call<Token> login( @Body LoginWrapper user );            
            }
    
    ```
    
5) Create the classes required by the login method *Token* and *LoginWrapper*:

   ``` java
           public class Token
           {
               private String accessToken;
           
               public String getAccessToken()
               {
                   return accessToken;
               }
           
           }
           
           public class LoginWrapper
           {
           
               private final String username;
           
               private final String password;
           
               public LoginWrapper( String username, String password )
               {
                   this.username = username;
                   this.password = password;
               }
           
               public String getUsername()
               {
                   return username;
               }
           
               public String getPassword()
               {
                   return password;
               }
           }
      
      ```
 
 6) Create a Network interface and the generic RequestCallback as follows:
 
    ``` java
        public interface Network
         {
             void login( LoginWrapper loginWrapper, RequestCallback<Token> requestCallback );
         }
                 
                 
         public interface RequestCallback<T>
             {
                 
                 void onSuccess( T response );
             
                 void onFailed( NetworkException e );
                 
             }
      ```
     

7) Create an implementation of the *Network* interface called *RetrofitNetwork* using the following code

    ``` java
    public class RetrofitNetwork
        implements Network
    {
    
        private static final String BASE_URL = "http://10.0.2.2:8080/";
    
        private NetworkService networkService;
    
        private ExecutorService backgroundExecutor = Executors.newFixedThreadPool( 1 );
    
        public RetrofitNetwork()
        {
            Retrofit retrofit =
                new Retrofit.Builder().baseUrl( BASE_URL ).addConverterFactory( GsonConverterFactory.create() ).build();
            azureService = retrofit.create( NetworkService.class );
        }
    
        @Override
        public void login( LoginWrapper loginWrapper, RequestCallback<Token> requestCallback )
        {
            Call<Token> call = networkService.login( loginWrapper );
            try
            {
                Response<Token> execute = call.execute();
    
                if ( execute.errorBody() != null )
                {
                    processServerError( requestCallback, execute.errorBody().byteStream() );
                }
                else
                {
    
                    requestCallback.onSuccess( execute.body() );
                }
            }
            catch ( IOException e )
            {
                requestCallback.onFailed( new NetworkException( 0, null, e ) );
            }
        }

      ```
     
8) Create the custom NetworkException adn implement the processServerError method that displays a message to user.


9) Connect your App to the network logic so when you click the Login button then the login method on the Network layer is called. Try the debugger to verify that the server response a token.



     