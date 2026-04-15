import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private currentLang = new BehaviorSubject<string>('en');
  private translations: any = {};

  public currentLang$ = this.currentLang.asObservable();

  constructor(private http: HttpClient) {
    this.loadTranslations('en');
  }

  async loadTranslations(lang: string) {
    const data = await firstValueFrom(this.http.get(`./assets/i18n/${lang}.json`));
    this.translations = data;
    this.currentLang.next(lang);
  }

  translate(key: string): string {
    return this.translations[key] || key;
  }

  setLanguage(lang: string) {
    this.loadTranslations(lang);
  }

  getLanguage() {
    return this.currentLang.value;
  }
}
