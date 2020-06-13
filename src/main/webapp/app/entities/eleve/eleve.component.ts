import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEleve } from 'app/shared/model/eleve.model';
import { EleveService } from './eleve.service';
import { EleveDeleteDialogComponent } from './eleve-delete-dialog.component';

@Component({
  selector: 'jhi-eleve',
  templateUrl: './eleve.component.html'
})
export class EleveComponent implements OnInit, OnDestroy {
  eleves?: IEleve[];
  eventSubscriber?: Subscription;

  constructor(
    protected eleveService: EleveService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.eleveService.query().subscribe((res: HttpResponse<IEleve[]>) => (this.eleves = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEleves();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEleve): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInEleves(): void {
    this.eventSubscriber = this.eventManager.subscribe('eleveListModification', () => this.loadAll());
  }

  delete(eleve: IEleve): void {
    const modalRef = this.modalService.open(EleveDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eleve = eleve;
  }
}
