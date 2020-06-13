import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IDiscipline, Discipline } from 'app/shared/model/discipline.model';
import { DisciplineService } from './discipline.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IEleve } from 'app/shared/model/eleve.model';
import { EleveService } from 'app/entities/eleve/eleve.service';

@Component({
  selector: 'jhi-discipline-update',
  templateUrl: './discipline-update.component.html'
})
export class DisciplineUpdateComponent implements OnInit {
  isSaving = false;
  eleves: IEleve[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    description: [],
    comment: [],
    eleve: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected disciplineService: DisciplineService,
    protected eleveService: EleveService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ discipline }) => {
      if (!discipline.id) {
        const today = moment().startOf('day');
        discipline.date = today;
      }

      this.updateForm(discipline);

      this.eleveService.query().subscribe((res: HttpResponse<IEleve[]>) => (this.eleves = res.body || []));
    });
  }

  updateForm(discipline: IDiscipline): void {
    this.editForm.patchValue({
      id: discipline.id,
      date: discipline.date ? discipline.date.format(DATE_TIME_FORMAT) : null,
      description: discipline.description,
      comment: discipline.comment,
      eleve: discipline.eleve
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('schoolManagementApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const discipline = this.createFromForm();
    if (discipline.id !== undefined) {
      this.subscribeToSaveResponse(this.disciplineService.update(discipline));
    } else {
      this.subscribeToSaveResponse(this.disciplineService.create(discipline));
    }
  }

  private createFromForm(): IDiscipline {
    return {
      ...new Discipline(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      eleve: this.editForm.get(['eleve'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiscipline>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IEleve): any {
    return item.id;
  }
}
