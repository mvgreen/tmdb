package com.mvgreen.tmdbapp.ui.base.event

abstract class Event

class LoginFailedEvent(val e: Throwable): Event()