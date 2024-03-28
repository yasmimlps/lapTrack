import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';

class CounterCard extends StatelessWidget {
  final String title;
  final int count;
  final VoidCallback increment;
  final VoidCallback decrement;
  const CounterCard(
      {Key? key,
      required this.title,
      required this.count,
      required this.increment,
      required this.decrement})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      shadowColor: Colors.grey,
      elevation: 5,
      color: Colors.amber,
      child: Padding(
        padding: const EdgeInsets.all(15),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(title, style: const TextStyle(fontSize: 20)),
            const SizedBox(height: 10),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                IconButton(
                  icon: const Icon(Icons.remove),
                  onPressed: decrement,
                ),
                const SizedBox(
                  width: 100,
                ),
                Text(
                  '$count',
                  style: const TextStyle(fontSize: 40),
                ),
                const SizedBox(
                  width: 100,
                ),
                IconButton(
                  icon: const Icon(Icons.add),
                  onPressed: increment,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
