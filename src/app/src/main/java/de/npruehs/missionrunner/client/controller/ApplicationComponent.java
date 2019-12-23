package de.npruehs.missionrunner.client.controller;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AccountModule.class)
public interface ApplicationComponent {
    AccountComponent.Factory accountComponent();
}
