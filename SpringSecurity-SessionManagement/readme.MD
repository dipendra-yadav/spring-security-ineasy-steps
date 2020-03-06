### session -management in spring security
###sessionCreationPolicy-

{  http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  }

we can define sessionCreationPolicy policies- There are four
ALWAYS- will create a session Always
NEVER- never create by itself , but if there is a session  available it will use that session.
       as if say your using spring MVC if that framework is creating a session , your spring security will use that same session.
 
IF_REQUIRED- only if it is required//default strategy
STATELESS- if your application is stateless(REST) , then spring security will never  use session to obtain the securityContext.       




### concurrency-control //concurrent session control

{ .maximumSessions(1).expiredUrl("/login") }

Now for the concurrency control you can specify number of session you want, specify 2/3 whatsoever u want.
if user goes beyond specify sessions redirect him to expiredUrl("/login")

### expired URL

-The URL to redirect to if a user tries to access a resource and their sessions has been expired dues to too many sessions for the current user.

### Invalid session URL
The URL to redirect when a invalid session is detected.

{ invalidSessionUrl("/login") }-This works when your user is invalidated that means when he is Loggedout & ur trying to login with the same session-Id it throws  this URL
  But at time sit can give you false alarm.
  
  Note: once you logout invalidateHttpSession  will always sets true by default.
  {  http.logout().deleteCookies("JSESSIONID").invalidateHttpSession(true); }

### session Fixation attack Protection

{  .sessionFixation().changeSessionId();//sessionFixation strategy-default }




## session scope Bean//if at all you have some Bean who is not stateless & storing states
@Component
@Scope(value="session",proxyMode = ScopedProxyMode.TARGET_CLASS)
//proxyMode- when no session is created & still this beans needs to be injected in HomeController as a request scope.

public class UserTo {

    private String username;
    private String password;
    
   
}


