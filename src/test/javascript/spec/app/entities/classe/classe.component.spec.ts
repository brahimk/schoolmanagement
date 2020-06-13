import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SchoolManagementTestModule } from '../../../test.module';
import { ClasseComponent } from 'app/entities/classe/classe.component';
import { ClasseService } from 'app/entities/classe/classe.service';
import { Classe } from 'app/shared/model/classe.model';

describe('Component Tests', () => {
  describe('Classe Management Component', () => {
    let comp: ClasseComponent;
    let fixture: ComponentFixture<ClasseComponent>;
    let service: ClasseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [ClasseComponent]
      })
        .overrideTemplate(ClasseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClasseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClasseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Classe(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.classes && comp.classes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
