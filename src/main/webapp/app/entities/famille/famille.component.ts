import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamille } from 'app/shared/model/famille.model';
import { FamilleService } from './famille.service';
import { FamilleDeleteDialogComponent } from './famille-delete-dialog.component';

@Component({
  selector: 'jhi-famille',
  templateUrl: './famille.component.html'
})
export class FamilleComponent implements OnInit, OnDestroy {
  familles?: IFamille[];
  eventSubscriber?: Subscription;

  constructor(protected familleService: FamilleService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.familleService.query().subscribe((res: HttpResponse<IFamille[]>) => (this.familles = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFamilles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFamille): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFamilles(): void {
    this.eventSubscriber = this.eventManager.subscribe('familleListModification', () => this.loadAll());
  }

  delete(famille: IFamille): void {
    const modalRef = this.modalService.open(FamilleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.famille = famille;
  }
}
