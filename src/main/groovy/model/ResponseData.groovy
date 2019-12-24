package model

class ResponseData {
  private int id;
  private String username;

  ResponseData(int id, String username) {
    this.id = id
    this.username = username
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
