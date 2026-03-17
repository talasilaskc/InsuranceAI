import { Component, AfterViewInit } from '@angular/core';
import { RouterLink } from '@angular/router';

declare const lucide: any;

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements AfterViewInit {
  policies = [
    {
      title: 'Algorithmic Liability',
      description: 'Coverage for errors, omissions, and unintended consequences of machine learning models.',
      icon: 'brain-circuit'
    },
    {
      title: 'Autonomous Systems',
      description: 'Physical and digital protection for independent agents and robotics platforms.',
      icon: 'bot'
    },
    {
      title: 'Generative AI Shield',
      description: 'Protection against copyright infringement, hallucination risks, and data leakage.',
      icon: 'sparkles'
    }
  ];

  ngAfterViewInit() {
    if (typeof lucide !== 'undefined') {
      lucide.createIcons();
    }
  }
}
