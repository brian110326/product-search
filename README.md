# 🛍️ Elasticsearch Project

---

## 🎯 프로젝트 목적

패션 상품 데이터를 통합하여 Elasticsearch 기반 검색과 가격 비교 기능을 제공하고,  
비동기 + 배치 + 파이프라인 구조로 대용량 검색 시스템 성능을 개선하는 것을 목표로 합니다.

---

## 📌 주요 기능

- Elasticsearch 기반 상품 검색 (자동완성, 필터, Highlight, Fuzzy Search)
- 가격 / 카테고리 / 평점 기반 조건 검색 및 랭킹 기능
- Kafka 기반 비동기 상품 인덱싱 처리
- Batch + Indexing Pipeline 기반 성능 최적화

---

## 📌 프로젝트 구현 사항

---

## 1️⃣ 검색 시스템 (Elasticsearch Search Engine)

- Elasticsearch 기반 상품 검색 엔진 구축
- MultiMatch Query 기반 키워드 검색
- Fuzzy Search를 통한 오타 허용 검색
- 자동완성 (BoolPrefix + Edge Ngram)
- 카테고리 / 가격 / 평점 필터링
- Highlight 기능으로 검색어 시각적 강조

---

## 2️⃣ 비동기 인덱싱 아키텍처 (Kafka + Pipeline)

- Kafka 기반 이벤트 드리븐 구조 적용
- 상품 생성 시 비동기 indexing 처리
- API 응답과 Elasticsearch indexing 분리
- Validation → Transformation → Batch → Bulk Pipeline 구조 설계

---

## 3️⃣ Batch 기반 성능 최적화

- 500개 단위 Batch 처리
- Elasticsearch Bulk API 활용
- 네트워크 호출 최소화
- indexing throughput 개선

---

## ⚡ 동기 → 비동기 전환을 통한 DB / Elasticsearch 동기화 개선

기존 구조에서는 상품 생성 시 DB 저장과 Elasticsearch 인덱싱이 **동기적으로 처리**되어  
API 응답 시간이 증가하고, ES 인덱싱 성능이 전체 시스템의 병목으로 작용하는 문제가 있었습니다.

이를 해결하기 위해 Kafka 기반 비동기 구조로 전환하여  
DB 저장과 ES 인덱싱을 분리하고, Batch + Pipeline 기반 처리 구조를 적용했습니다.

---

### 🔴 기존 구조 (Sync)

- DB 저장과 ES 인덱싱이 동시에 수행
- Indexing 완료까지 API 응답 지연 발생
- 트래픽 증가 시 병목 발생

---

### 🟢 개선 구조 (Async + Event Driven)

- DB 저장과 ES 인덱싱 완전 분리
- Kafka 기반 비동기 이벤트 처리
- Batch + Bulk Indexing 적용
- API는 즉시 응답 처리

---

## 📊 성능 개선 결과

동기 → 비동기 구조 전환 및 DB / ES 동기화 분리 후 성능은 다음과 같이 개선되었습니다.

| 지표          | Before (Sync) | After (Async) | 개선율          |
|-------------|---------------|---------------|--------------|
| avg latency | 102ms         | 18ms          | 🔥 81% 감소    |
| p95 latency | 181ms         | 31ms          | 🔥 82% 감소    |
| max latency | 475ms         | 165ms         | 🔥 65% 감소    |
| TPS         | 546           | 2944          | 🚀 약 5.4배 증가 |

---

## 🧠 개선 핵심 포인트

- DB 저장과 Elasticsearch indexing 완전 분리
- Kafka 기반 비동기 이벤트 처리 구조 적용
- Bulk Indexing을 통한 ES write 성능 최적화
- Batch processing으로 네트워크 비용 감소
- API 응답과 데이터 동기화 로직 분리 (CQRS 구조)

---

## 📌 결과

이 개선을 통해  
**검색 인덱싱이 API 성능에 영향을 주지 않는 구조로 전환되었으며**,  
대용량 트래픽 환경에서도 안정적인 응답 성능을 확보할 수 있었습니다.

---

## 🧠 핵심 설계

- CQRS 구조 (Write / Read 분리)
- Event Driven Architecture (Kafka)
- Indexing Pipeline 구조
- Bulk Processing Optimization
- Search Relevance Tuning

---

## 🛠 기술 스택

- Java 17
- Spring Boot
- Spring Data Elasticsearch
- Kafka
- MySql
- Docker

---