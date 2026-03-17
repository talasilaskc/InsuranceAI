import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../../components/sidebar/sidebar';

@Component({
  selector: 'app-officer-layout',
  standalone: true,
  imports: [RouterOutlet, Sidebar],
  template: `
    <app-sidebar role="officer"></app-sidebar>
    <div class="main-content">
      <router-outlet></router-outlet>
    </div>
  `
})
export class OfficerLayout {}
