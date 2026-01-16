import { Component, Input } from '@angular/core';
@Component({
  selector: 'app-error-message',
  standalone: true,
  template: `<div class="bg-red-100 text-red-700 p-4 rounded">{{message}}</div>`
})
export class ErrorMessageComponent {
  @Input() message = '';
}