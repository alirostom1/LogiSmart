import { Component, Input } from '@angular/core';
@Component({
  selector: 'app-empty-state',
  standalone: true,
  template: `<div class="text-center py-12 text-gray-400">{{message}}</div>`
})
export class EmptyStateComponent {
  @Input() message = 'No data';
}