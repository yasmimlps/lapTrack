import 'package:flutter/material.dart';
import 'package:laptrack/home/home_page.dart';

void main() {
  runApp(const LapTrack());
}

class LapTrack extends StatelessWidget {
  const LapTrack({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primarySwatch: Colors.amber,
      ),
      home: HomePage(),
    );
  }
}
