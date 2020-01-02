package model

class AuthenticationResponse {
  private int id;
  private String username;
  private String jwtToken;

  AuthenticationResponse(int id, String username, String jwt) {
    this.id = id;
    this.username = username;
    this.jwtToken = jwt
  }

  String getJwtToken() {
    return jwtToken
  }

  void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken
  }

  int getId() {
    return id
  }

  void setId(int id) {
    this.id = id
  }

  String getUsername() {
    return username
  }

  void setUsername(String username) {
    this.username = username
  }
}
