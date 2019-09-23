import 'package:example/home.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';

void main() => runApp(App());

class App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return MaterialApp(
      title: "annotation example",
      theme: ThemeData(
        primaryColor: Colors.red,
      ),
      home: new Scaffold(backgroundColor:Colors.grey,body:MyApp(),)
    );
  }
}

class MyApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return MyAppState();
  }
}

class MyAppState extends State<MyApp> {
  GlobalKey<FormState> _signInFormKey = new GlobalKey();
  TextEditingController _userNameEditingController =
      new TextEditingController();
  TextEditingController _passwordEditingController =
      new TextEditingController();
  String _username = "";
  String _pwd = "";
  bool _isShowpwd = false;
  bool isLoading = false;
  static const channel = const MethodChannel("com.lcj.example/login");

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new Scaffold(
      body: SingleChildScrollView(
        child: Container(
          decoration: BoxDecoration(
            color: Colors.grey,
          ),
          height: MediaQuery.of(context).size.height,
          width: MediaQuery.of(context).size.width,
          child: Center(
            child: Container(
              width: MediaQuery.of(context).size.width * 0.85,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.all(Radius.circular(8.0)),
                color: Colors.white,
              ),
              child: new Stack(
                children: <Widget>[
                  new Column(
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      _buildForm(context),
                      SizedBox(
                        height: 35.0,
                      ),
                      _buildSignInBtn(context),
                      SizedBox(
                        height: 35.0,
                      ),
                    ],
                  ),
                  Positioned(
                    top: 0,
                    left: 0,
                    bottom: 0,
                    child: _buildLoading(context),
                  )
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildLoading(BuildContext context) {
    if (isLoading) {
      return Opacity(
        opacity: .5,
        child: Container(
          width: MediaQuery.of(context).size.width * 0.85,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.all(Radius.circular(8.0)),
            color: Colors.black,
          ),
          child: SpinKitPouringHourglass(color: Colors.white),
        ),
      );
    }
    return Container();
  }

  Widget _buildForm(BuildContext context) {
    return new Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.all(Radius.circular(8.0)),
      ),
      width: MediaQuery.of(context).size.width * 0.8,
      height: 190,
      //表单
      child: Form(
        key: _signInFormKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            Flexible(
                child: Padding(
              padding: const EdgeInsets.only(
                left: 25,
                right: 25,
                top: 20,
                bottom: 20,
              ),
              child: TextFormField(
                controller: _userNameEditingController,
                decoration: InputDecoration(
                    icon: Icon(Icons.email, color: Colors.black),
                    hintText: '登录名',
                    border: InputBorder.none),
                style: TextStyle(fontSize: 16, color: Colors.black),
                validator: (value) {
                  if (value == null || value.isEmpty) return "登录名不能为空";
                  return null;
                },
                onSaved: (value) {
                  setState(() {
                    _username = value;
                  });
                },
              ),
            )),
            Container(
              height: 1,
              width: MediaQuery.of(context).size.width * 0.75,
              color: Colors.grey[400],
            ),
            Flexible(
              child: Padding(
                padding: const EdgeInsets.only(left: 25, right: 25, top: 20),
                child: TextFormField(
                  controller: _passwordEditingController,
                  decoration: InputDecoration(
                    icon: Icon(
                      Icons.lock,
                      color: Colors.black,
                    ),
                    hintText: "登录密码",
                    border: InputBorder.none,
                    suffixIcon: IconButton(
                      icon: Icon(
                        Icons.remove_red_eye,
                        color: Colors.black,
                      ),
                      onPressed: showPassWord,
                    ),
                  ),
                  //flutter这里是否设置输入为******显示
                  obscureText: !_isShowpwd,
                  style: TextStyle(fontSize: 16, color: Colors.black),
                  validator: (value) {
                    if (value == null || value.isEmpty) return "密码不能为空!";
                    return null;
                  },
                  onSaved: (value) {
                    setState(() {
                      _pwd = value;
                    });
                  },
                ),
              ),
            ),
            Container(
              height: 1,
              width: MediaQuery.of(context).size.width * 0.75,
              color: Colors.grey[400],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSignInBtn(BuildContext context) {
    return GestureDetector(
      onTap: () {
        if (_signInFormKey.currentState.validate()) {
          _doLogin();
        }
      },
      child: Container(
        padding: EdgeInsets.only(left: 42, right: 42, top: 10, bottom: 10),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.all(Radius.circular(5)),
          color: Theme.of(context).primaryColor,
        ),
        child: Text(
          "登录",
          style: TextStyle(fontSize: 25, color: Colors.white),
        ),
      ),
    );
  }

  void showPassWord() {
    setState(() {
      _isShowpwd = !_isShowpwd;
    });
  }

  void _doLogin() async {
    _signInFormKey.currentState.save();
    setState(() {
      isLoading = true;
    });
    final result = await channel
        .invokeMethod("doLogin", {"userName":_username, "password":_pwd});
    final map = Map<String, String>.from(result);
    print("return $map");
    setState(() {
      isLoading = false;
    });

    String un = map['username'];
    Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(
          builder: (context) => HomePage(un),
        ),
        (route) => route == null);
  }
}
