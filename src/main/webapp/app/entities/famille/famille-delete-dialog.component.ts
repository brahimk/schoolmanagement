import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFamille } from 'app/shared/model/famille.model';
import { FamilleService } from './famille.service';

@Component({
  templateUrl: './famille-delete-dialog.component.html'
})
export class FamilleDeleteDialogComponent {
  famille?: IFamille;

  constructor(protected familleService: FamilleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('familleListModification');
      this.activeModal.close();
    });
  }
}
