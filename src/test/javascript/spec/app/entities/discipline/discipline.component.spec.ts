import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SchoolManagementTestModule } from '../../../test.module';
import { DisciplineComponent } from 'app/entities/discipline/discipline.component';
import { DisciplineService } from 'app/entities/discipline/discipline.service';
import { Discipline } from 'app/shared/model/discipline.model';

describe('Component Tests', () => {
  describe('Discipline Management Component', () => {
    let comp: DisciplineComponent;
    let fixture: ComponentFixture<DisciplineComponent>;
    let service: DisciplineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [DisciplineComponent]
      })
        .overrideTemplate(DisciplineComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DisciplineComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DisciplineService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Discipline(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.disciplines && comp.disciplines[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
