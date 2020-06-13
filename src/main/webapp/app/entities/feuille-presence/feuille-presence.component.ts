import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFeuillePresence } from 'app/shared/model/feuille-presence.model';
import { FeuillePresenceService } from './feuille-presence.service';
import { FeuillePresenceDeleteDialogComponent } from './feuille-presence-delete-dialog.component';

@Component({
  selector: 'jhi-feuille-presence',
  templateUrl: './feuille-presence.component.html'
})
export class FeuillePresenceComponent implements OnInit, OnDestroy {
  feuillePresences?: IFeuillePresence[];
  eventSubscriber?: Subscription;

  constructor(
    protected feuillePresenceService: FeuillePresenceService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.feuillePresenceService.query().subscribe((res: HttpResponse<IFeuillePresence[]>) => (this.feuillePresences = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFeuillePresences();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFeuillePresence): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInFeuillePresences(): void {
    this.eventSubscriber = this.eventManager.subscribe('feuillePresenceListModification', () => this.loadAll());
  }

  delete(feuillePresence: IFeuillePresence): void {
    const modalRef = this.modalService.open(FeuillePresenceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.feuillePresence = feuillePresence;
  }
}
