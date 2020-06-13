import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFeuillePresence } from 'app/shared/model/feuille-presence.model';
import { FeuillePresenceService } from './feuille-presence.service';

@Component({
  templateUrl: './feuille-presence-delete-dialog.component.html'
})
export class FeuillePresenceDeleteDialogComponent {
  feuillePresence?: IFeuillePresence;

  constructor(
    protected feuillePresenceService: FeuillePresenceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feuillePresenceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('feuillePresenceListModification');
      this.activeModal.close();
    });
  }
}
