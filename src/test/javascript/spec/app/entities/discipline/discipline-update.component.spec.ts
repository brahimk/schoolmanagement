import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SchoolManagementTestModule } from '../../../test.module';
import { DisciplineUpdateComponent } from 'app/entities/discipline/discipline-update.component';
import { DisciplineService } from 'app/entities/discipline/discipline.service';
import { Discipline } from 'app/shared/model/discipline.model';

describe('Component Tests', () => {
  describe('Discipline Management Update Component', () => {
    let comp: DisciplineUpdateComponent;
    let fixture: ComponentFixture<DisciplineUpdateComponent>;
    let service: DisciplineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [DisciplineUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DisciplineUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DisciplineUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DisciplineService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Discipline(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Discipline();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
