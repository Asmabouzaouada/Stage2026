import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ApplicationResponse } from '../../../core/models/application.model';

@Component({
  selector: 'app-kanban-card',
  standalone: true,
  imports: [],
  templateUrl: './kanban-card.component.html',
  styleUrl: './kanban-card.component.css'
})
export class KanbanCardComponent {
  @Input() application!: ApplicationResponse;
  @Output() decisionClicked = new EventEmitter<void>();
}