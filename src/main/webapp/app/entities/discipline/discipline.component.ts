import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDiscipline } from 'app/shared/model/discipline.model';
import { DisciplineService } from './discipline.service';
import { DisciplineDeleteDialogComponent } from './discipline-delete-dialog.component';

@Component({
  selector: 'jhi-discipline',
  templateUrl: './discipline.component.html'
})
export class DisciplineComponent implements OnInit, OnDestroy {
  disciplines?: IDiscipline[];
  eventSubscriber?: Subscription;

  constructor(
    protected disciplineService: DisciplineService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.disciplineService.query().subscribe((res: HttpResponse<IDiscipline[]>) => (this.disciplines = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDisciplines();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDiscipline): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInDisciplines(): void {
    this.eventSubscriber = this.eventManager.subscribe('disciplineListModification', () => this.loadAll());
  }

  delete(discipline: IDiscipline): void {
    const modalRef = this.modalService.open(DisciplineDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.discipline = discipline;
  }
}
