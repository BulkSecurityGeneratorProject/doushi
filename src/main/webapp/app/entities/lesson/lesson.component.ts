import { Component, OnInit, OnDestroy, ViewChildren } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Observable } from 'rxjs/Observable';
import { Principal, User } from '../../shared';

import { Answer } from '../answer/answer.model';
import { AnswerService } from '../answer/answer.service';
import { Verb } from '../verb/verb.model';
import { VerbService } from '../verb/verb.service';
import { ConjugatedVerb } from '../conjugated-verb/conjugated-verb.model';
import { ConjugatedVerbService } from '../conjugated-verb/conjugated-verb.service';
declare var wanakana: any;

@Component({
    selector: 'jhi-verb',
    templateUrl: './lesson.component.html'
})
export class LessonComponent implements OnInit, OnDestroy {

    verb: Verb;
    conjugatedVerbs: ConjugatedVerb[];
    currentAccount: any;
    eventSubscriber: Subscription;
    predicate: any;
    correct: boolean;
    user: User;
    isError: boolean;
    nothingMoreToStudy: boolean;
    @ViewChildren('inputFocus') inputFocus;

    constructor(
        private verbService: VerbService,
        private conjugatedVerbService: ConjugatedVerbService,
        private answerService: AnswerService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal,
    ) {
        this.isError = false;
        this.nothingMoreToStudy = false;
        this.correct = undefined;
        this.principal = principal;
    }

    loadAll() {
        this.verbService.findForStudy({lesson: true}).subscribe(
            (res: HttpResponse<Verb>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    reset() {
        this.verb = undefined;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        setTimeout(() => {
          if (this.inputFocus && this.inputFocus.first) {
            wanakana.bind(this.inputFocus.first.nativeElement);
            this.inputFocus.first.nativeElement.focus();
          }
        }, 1000);
    }

    ngOnDestroy() {
    }

    check() {
      // if they didn't provide an answer at all do nothing
      if (!this.verb.answer) {
        return;
      }
      if (wanakana.toKana(this.verb.answer) === wanakana.toKana(this.verb.romanjiText)) {
        this.correct = true;
      } else {
        // hack to get the display text to be in kana
        this.verb.kanaText = wanakana.toKana(this.verb.romanjiText);
        this.correct = false;
      }

      const a = new Answer(
        undefined,
        this.correct,
        undefined, // let the backend fill the date in
        wanakana.toKana(this.verb.answer),
        this.currentAccount,
        this.verb,
        undefined
      );
      this.subscribeToSaveAnswerResponse(
          this.answerService.create(a));
    }

    private subscribeToSaveAnswerResponse(result: Observable<HttpResponse<Answer>>) {
        result.subscribe((res: HttpResponse<Answer>) =>
            this.onSaveAnswerSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveAnswerSuccess(result: Answer) {
        if (this.correct) {
          setTimeout(() => { this.next(); }, 2000);
          this.eventManager.broadcast({
              name: 'quizTaken',
              content: 'Quiz Was Taken'
          });
        }
    }

    private onSaveError() {
        this.isError = true;
        this.jhiAlertService.error('doushiApp.answer.error', null, null);
    }

  　next() {
      this.correct = undefined;
      this.loadAll();
    }

    private onSuccess(data, headers) {
        this.verb = data;
        this.conjugatedVerbService.queryByVerb(this.verb.id)
            .subscribe((conjugatedVerbResponse: HttpResponse<ConjugatedVerb[]>) => {
                this.conjugatedVerbs = conjugatedVerbResponse.body;
            });
        setTimeout(() => {
          if (this.inputFocus && this.inputFocus.first) {
            this.inputFocus.first.nativeElement.focus();
          }
        }, 1000);
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
        if (error.indexOf('404') > -1) {
          this.nothingMoreToStudy = true;
          this.verb = undefined;
        }
    }
}
