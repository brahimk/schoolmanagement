import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProfesseur } from 'app/shared/model/professeur.model';
import { ProfesseurService } from './professeur.service';
import { ProfesseurDeleteDialogComponent } from './professeur-delete-dialog.component';

@Component({
  selector: 'jhi-professeur',
  templateUrl: './professeur.component.html'
})
export class ProfesseurComponent implements OnInit, OnDestroy {
  professeurs?: IProfesseur[];
  eventSubscriber?: Subscription;

  constructor(protected professeurService: ProfesseurService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.professeurService.query().subscribe((res: HttpResponse<IProfesseur[]>) => (this.professeurs = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProfesseurs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProfesseur): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProfesseurs(): void {
    this.eventSubscriber = this.eventManager.subscribe('professeurListModification', () => this.loadAll());
  }

  delete(professeur: IProfesseur): void {
    const modalRef = this.modalService.open(ProfesseurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.professeur = professeur;
  }
}
