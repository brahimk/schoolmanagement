import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClasse } from 'app/shared/model/classe.model';
import { ClasseService } from './classe.service';

@Component({
  templateUrl: './classe-delete-dialog.component.html'
})
export class ClasseDeleteDialogComponent {
  classe?: IClasse;

  constructor(protected classeService: ClasseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('classeListModification');
      this.activeModal.close();
    });
  }
}
