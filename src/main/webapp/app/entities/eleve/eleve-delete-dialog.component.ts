import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEleve } from 'app/shared/model/eleve.model';
import { EleveService } from './eleve.service';

@Component({
  templateUrl: './eleve-delete-dialog.component.html'
})
export class EleveDeleteDialogComponent {
  eleve?: IEleve;

  constructor(protected eleveService: EleveService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eleveService.delete(id).subscribe(() => {
      this.eventManager.broadcast('eleveListModification');
      this.activeModal.close();
    });
  }
}
