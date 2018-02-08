import { ApiServicesService } from './shared/api-services.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { CapitalizePipe } from './pipes/capitalize.pipe';


@NgModule({
  declarations: [
    AppComponent,
    CapitalizePipe
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [HttpClient, ApiServicesService],
  bootstrap: [AppComponent]
})
export class AppModule { }
