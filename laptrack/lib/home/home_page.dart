import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:laptrack/components/counter_card.dart';
import 'package:laptrack/shared/counter_store.dart';

class HomePage extends StatelessWidget {
  HomePage({Key? key}) : super(key: key);
  final CounterStore counterStore = CounterStore();

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 4,
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Contador de Voltas'),
        ),
        body: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Center(
                child: Observer(
                  builder: (_) => CounterCard(
                    count: counterStore.count,
                    increment: () {
                      counterStore.increment();
                    },
                    decrement: () {
                      counterStore.decrement();
                    },
                    title: 'Equipe 1',
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
