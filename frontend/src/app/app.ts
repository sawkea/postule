import { Component, signal } from '@angular/core';
import { ApplicationList } from './applications/application-list';

@Component({
  selector: 'app-root',
  imports: [ApplicationList],
  styleUrl: './app.scss',
  templateUrl: './app.html',
  
})
export class App {
  protected readonly title = signal('frontend');
}
