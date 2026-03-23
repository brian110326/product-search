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