import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class HomePage extends StatelessWidget {

  String username;

  HomePage(this.username);

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      appBar: AppBar(
        title: Text("home"),
        centerTitle: true,
      ),
      body: Center(
        child: Text('username:$username',style: TextStyle(fontSize: 18),),
      ),
    );
  }

}