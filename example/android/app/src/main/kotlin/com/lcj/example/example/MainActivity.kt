package com.lcj.example.example

import android.os.Bundle
import com.lcj.flutter_channel_annotation_ioc_api.FlutterChannelFactory

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
    FlutterChannelFactory.register(this)
  }
}
