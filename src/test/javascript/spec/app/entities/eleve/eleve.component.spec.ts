import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SchoolManagementTestModule } from '../../../test.module';
import { EleveComponent } from 'app/entities/eleve/eleve.component';
import { EleveService } from 'app/entities/eleve/eleve.service';
import { Eleve } from 'app/shared/model/eleve.model';

describe('Component Tests', () => {
  describe('Eleve Management Component', () => {
    let comp: EleveComponent;
    let fixture: ComponentFixture<EleveComponent>;
    let service: EleveService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [EleveComponent]
      })
        .overrideTemplate(EleveComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EleveComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EleveService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Eleve(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.eleves && comp.eleves[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
