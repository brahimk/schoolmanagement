import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SchoolManagementTestModule } from '../../../test.module';
import { FeuillePresenceComponent } from 'app/entities/feuille-presence/feuille-presence.component';
import { FeuillePresenceService } from 'app/entities/feuille-presence/feuille-presence.service';
import { FeuillePresence } from 'app/shared/model/feuille-presence.model';

describe('Component Tests', () => {
  describe('FeuillePresence Management Component', () => {
    let comp: FeuillePresenceComponent;
    let fixture: ComponentFixture<FeuillePresenceComponent>;
    let service: FeuillePresenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SchoolManagementTestModule],
        declarations: [FeuillePresenceComponent]
      })
        .overrideTemplate(FeuillePresenceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FeuillePresenceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FeuillePresenceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FeuillePresence(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.feuillePresences && comp.feuillePresences[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
