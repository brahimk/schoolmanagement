import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDiscipline } from 'app/shared/model/discipline.model';
import { DisciplineService } from './discipline.service';

@Component({
  templateUrl: './discipline-delete-dialog.component.html'
})
export class DisciplineDeleteDialogComponent {
  discipline?: IDiscipline;

  constructor(
    protected disciplineService: DisciplineService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.disciplineService.delete(id).subscribe(() => {
      this.eventManager.broadcast('disciplineListModification');
      this.activeModal.close();
    });
  }
}
