import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());


class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  List resultados=List();
  List infoDato = List();
  String _result;
  String clientId = "AXIAUMr5Wvw1ArUNzexxNr_BzxecGVZhCDkKe66lXqAhw4CHRMIyr6oTXsdSiBWgcZjFFWWVlBpZJkKI"; //clientId de paypal developer AXIAUMr5Wvw1ArUNzexxNr_BzxecGVZhCDkKe66lXqAhw4CHRMIyr6oTXsdSiBWgcZjFFWWVlBpZJkKI

  @override
  void initState() {

    super.initState();
    //this.enlistar();
  }
  
  final platform = MethodChannel('paypal');
  String result = '';

  List info = List();
  List datos = List();

 /* Future<List> payment()async{
    List listaTemp = List();
    String datoTemp;

    try{
      List dato = await platform.invokeListMethod('payment',{
        "precio": 10.0,
        "descripcion": "Este es una prueba",
        "lista": datos
      });
      listaTemp = dato;
    }on PlatformException catch(e){
       listaTemp = e.details;
    }
    setState(() {
       resultados = listaTemp;
    });
    print(resultados);
    return resultados;

  }*/

  Future<void> payment()async{
    await platform.invokeMethod('payment',{
      "precio": 10.0,
      "descripcion": "Este es una prueba",
      "lista": datos
      //"clientid": clientId
    });
  }

  Future<List> enlistar()async{


   infoDato = await platform.invokeListMethod('listaresult');
    print(infoDato);
   return infoDato;
  }



  @override
  Widget build(BuildContext context) {
  final size = MediaQuery.of(context).size;


  if(infoDato.isEmpty){
    Future.delayed(
        Duration(seconds: 3),(){
      setState(() {

      });
      enlistar();
    }
    );
  }else{
    print(infoDato);
  }
   /*Future.delayed(
       Duration(microseconds: 1),(){
         setState(() {

         });
         enlistar();
     }
   );*/


  /*if(infoDato[5] == "approved"){
    showDialog(
        context: context,
        builder: (context){
          return Scaffold(
            body: AlertDialog(
              title: Text('Pago aprobado'),
              content: Container(
                width: size.width * 0.5,
                height: size.height * 0.3,
                child: Column(
                  children: <Widget>[

                    Text('Tu pago fue un éxito',style: TextStyle(fontSize: 6.0),),
                    RaisedButton(
                      textTheme: ButtonTextTheme.primary,
                      color: Colors.green,
                      shape: StadiumBorder(),
                      child: Text('Aceptar',style: TextStyle(color: Colors.white),),
                      onPressed: (){
                        Navigator.pop(context);
                      },
                    ),

                  ],
                ),
              ),
              /* actions: <Widget>[
            FlatButton(
              child: Text('Ok'),
              onPressed: ()=>Navigator.of(context).pop(),
            )
          ],*/
            ),
          );
        }
    );
  }*/

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center, //infoDato.isEmpty
          children: <Widget>[
            infoDato.isEmpty ?
            RaisedButton(
              child: Text('Ingresar'),
              onPressed: (){

               payment();
               //enlistar();
              },
            ):RaisedButton(
              child: Text('Volver a detalles'),
              onPressed: (){

               // payment();
                //enlistar();
              },
            ),
          /*  FutureBuilder(
              future: enlistar(),
                builder: (context,snapshot){
                return Text(snapshot.data.toString());
                }
            )*/
          infoDato.isNotEmpty ?
           Text(
              'Pago aprobado',//infoDato[5] = approved
              //style: Theme.of(context).textTheme.display1,
            ):Text(
            '',//infoDato[5] = approved
            //style: Theme.of(context).textTheme.display1,
          ),
          ],
        ),
      ),
       // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}