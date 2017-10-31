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
            
                  @POST( "user/login" )
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
            networkService = retrofit.create( NetworkService.class );
        }
    
        @Override
        public void login( final LoginWrapper loginWrapper, final RequestCallback<Token> requestCallback )
        {
            backgroundExecutor.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    Call<Token> call = networkService.login( loginWrapper );
                    try
                    {
                        Response<Token> execute = call.execute();
                        requestCallback.onSuccess( execute.body() );
                    }
                    catch ( IOException e )
                    {
                        requestCallback.onFailed( new NetworkException( null, e ) );
                    }
                }
            } );
    
        }

    }

      ```
     
8) Create the custom NetworkException that extends the Exception class and has the constructor with a message and throwable.

9) Add a Internet permission to the AndroidManifest file:
      ```xml
      <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                package="com.eci.cosw.myapp">
      
        <uses-permission android:name="android.permission.INTERNET" />
      
        <application ...
   
      ```


10) Connect your Activity code to the network logic so when you click the Login button, then the login method on the Network layer is called. Try the debugger to verify that the server response with a token as expected.


#### Part 2: Consuming TODO API and adding token with interceptor


1) In order to consume the secure api you will need to add your token to each request you make to the server.
To achieve this we are going to use the following interceptor in the *RetrofitNetwork* class:

  ``` java
    public void addSecureTokenInterceptor( final String token )
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor( new Interceptor()
        {
            @Override
            public okhttp3.Response intercept( Chain chain )
                throws IOException
            {
                Request original = chain.request();

                Request request = original.newBuilder().header( "Accept", "application/json" ).header( "Authorization",
                                                                                                       "Bearer "
                                                                                                           + token ).method(
                    original.method(), original.body() ).build();
                return chain.proceed( request );
            }
        } );
        Retrofit retrofit =
            new Retrofit.Builder().baseUrl( BASE_URL ).addConverterFactory( GsonConverterFactory.create() ).client(
                httpClient.build() ).build();
        networkService = retrofit.create( NetworkService.class );
    }

  ```


2) We will use the *SharedPreferences* class to save the token so when we close our App and open it again we do not need to authenticate again:

    https://developer.android.com/guide/topics/data/data-storage.html#pref


3) Read and understand how to use the *SharedPreferences* so you save your token and you retrieve once the App is launched and started.


4) Add the methods getTodos and createTodo to the *NetworkService* with the correct API path.


5) Add the methods retrieve the TODO list and to create a TODO in the *Netowrk* interface.


6) Implement the methods retrieve the TODO list and to create a TODO in the *RetrofiNetowrk* class.


7) Create the proper layouts to display a TODO list and to create a TODO


8) Connect your UI with the network logic and make sure the App works as expected.





     