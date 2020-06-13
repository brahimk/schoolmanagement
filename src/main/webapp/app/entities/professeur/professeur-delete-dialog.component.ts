import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProfesseur } from 'app/shared/model/professeur.model';
import { ProfesseurService } from './professeur.service';

@Component({
  templateUrl: './professeur-delete-dialog.component.html'
})
export class ProfesseurDeleteDialogComponent {
  professeur?: IProfesseur;

  constructor(
    protected professeurService: ProfesseurService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.professeurService.delete(id).subscribe(() => {
      this.eventManager.broadcast('professeurListModification');
      this.activeModal.close();
    });
  }
}
