import { useState } from "react";

const BearFace = ({ x, y, size = 40, expression = "neutral" }) => {
  const s = size;
  return (
    <g transform={`translate(${x - s/2}, ${y - s/2})`}>
      {/* Ears */}
      <circle cx={s * 0.2} cy={s * 0.15} r={s * 0.15} fill="#C4956A" />
      <circle cx={s * 0.2} cy={s * 0.15} r={s * 0.08} fill="#E8C9A0" />
      <circle cx={s * 0.8} cy={s * 0.15} r={s * 0.15} fill="#C4956A" />
      <circle cx={s * 0.8} cy={s * 0.15} r={s * 0.08} fill="#E8C9A0" />
      {/* Head */}
      <circle cx={s * 0.5} cy={s * 0.45} r={s * 0.35} fill="#D4A574" />
      {/* Hair */}
      <ellipse cx={s * 0.5} cy={s * 0.18} rx={s * 0.22} ry={s * 0.12} fill="#2C2C2C" />
      <ellipse cx={s * 0.38} cy={s * 0.22} rx={s * 0.08} ry={s * 0.06} fill="#2C2C2C" />
      {/* Muzzle */}
      <ellipse cx={s * 0.5} cy={s * 0.52} rx={s * 0.18} ry={s * 0.12} fill="#F5E6D3" />
      {/* Eyes */}
      <circle cx={s * 0.38} cy={s * 0.42} r={s * 0.04} fill="#1a1a1a" />
      <circle cx={s * 0.62} cy={s * 0.42} r={s * 0.04} fill="#1a1a1a" />
      {/* Nose */}
      <ellipse cx={s * 0.5} cy={s * 0.48} rx={s * 0.045} ry={s * 0.035} fill="#1a1a1a" />
      {/* Mouth */}
      {expression === "happy" && (
        <path d={`M${s*0.43} ${s*0.54} Q${s*0.5} ${s*0.6} ${s*0.57} ${s*0.54}`} fill="none" stroke="#1a1a1a" strokeWidth={1.5} />
      )}
      {expression === "confused" && (
        <>
          <path d={`M${s*0.43} ${s*0.56} Q${s*0.5} ${s*0.53} ${s*0.57} ${s*0.56}`} fill="none" stroke="#1a1a1a" strokeWidth={1.5} />
          <text x={s * 0.72} y={s * 0.35} fontSize={s * 0.15} fill="#555">?</text>
        </>
      )}
      {expression === "neutral" && (
        <line x1={s*0.44} y1={s*0.55} x2={s*0.56} y2={s*0.55} stroke="#1a1a1a" strokeWidth={1.5} />
      )}
      {expression === "sweat" && (
        <>
          <path d={`M${s*0.43} ${s*0.56} Q${s*0.5} ${s*0.53} ${s*0.57} ${s*0.56}`} fill="none" stroke="#1a1a1a" strokeWidth={1.5} />
          <text x={s * 0.73} y={s * 0.3} fontSize={s * 0.12} fill="#4BA3F5">💧</text>
        </>
      )}
    </g>
  );
};

export default function ProductivityIllusion() {
  const [activeView, setActiveView] = useState("main");

  return (
    <div className="min-h-screen bg-gray-950 flex flex-col items-center justify-center p-4" style={{ fontFamily: "'Noto Sans KR', sans-serif" }}>
      {/* Tab Buttons */}
      <div className="flex gap-2 mb-6">
        <button
          onClick={() => setActiveView("main")}
          className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${
            activeView === "main"
              ? "bg-amber-500 text-gray-950"
              : "bg-gray-800 text-gray-400 hover:bg-gray-700"
          }`}
        >
          메인 컨셉
        </button>
        <button
          onClick={() => setActiveView("minmax")}
          className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${
            activeView === "minmax"
              ? "bg-amber-500 text-gray-950"
              : "bg-gray-800 text-gray-400 hover:bg-gray-700"
          }`}
        >
          Min / Max
        </button>
        <button
          onClick={() => setActiveView("question")}
          className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${
            activeView === "question"
              ? "bg-amber-500 text-gray-950"
              : "bg-gray-800 text-gray-400 hover:bg-gray-700"
          }`}
        >
          핵심 질문
        </button>
      </div>

      {/* Main Concept */}
      {activeView === "main" && (
        <div className="bg-gray-900 rounded-2xl p-1" style={{ width: 720, height: 480 }}>
          <svg viewBox="0 0 720 480" xmlns="http://www.w3.org/2000/svg">
            <defs>
              <linearGradient id="bg1" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#1a1a2e" />
                <stop offset="100%" stopColor="#0f0f1a" />
              </linearGradient>
              <linearGradient id="barGrad" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#F59E0B" />
                <stop offset="100%" stopColor="#D97706" />
              </linearGradient>
              <linearGradient id="barGradDim" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#6B7280" />
                <stop offset="100%" stopColor="#4B5563" />
              </linearGradient>
            </defs>
            <rect width="720" height="480" fill="url(#bg1)" rx="16" />

            {/* Title */}
            <text x="360" y="52" textAnchor="middle" fill="#F5F5F5" fontSize="28" fontWeight="bold">개발 생산성의 착각</text>
            <text x="360" y="78" textAnchor="middle" fill="#9CA3AF" fontSize="14">The Illusion of Developer Productivity</text>

            {/* Left Side - Speed Up */}
            <g transform="translate(80, 110)">
              <rect x="0" y="0" width="250" height="280" rx="12" fill="#1E293B" stroke="#334155" strokeWidth="1" />
              <text x="125" y="32" textAnchor="middle" fill="#F59E0B" fontSize="15" fontWeight="bold">⚡ 개발 속도 2x</text>

              {/* Speed bars */}
              <text x="30" y="70" fill="#9CA3AF" fontSize="11">Before</text>
              <rect x="30" y="78" width="190" height="18" rx="4" fill="#374151" />
              <rect x="30" y="78" width="90" height="18" rx="4" fill="url(#barGradDim)" />
              <text x="75" y="91" textAnchor="middle" fill="white" fontSize="10">3일</text>

              <text x="30" y="120" fill="#9CA3AF" fontSize="11">After (AI)</text>
              <rect x="30" y="128" width="190" height="18" rx="4" fill="#374151" />
              <rect x="30" y="128" width="190" height="18" rx="4" fill="url(#barGrad)" />
              <text x="125" y="141" textAnchor="middle" fill="white" fontSize="10" fontWeight="bold">30분</text>

              {/* Bear happy */}
              <BearFace x={125} y={195} size={60} expression="happy" />
              <text x="125" y="240" textAnchor="middle" fill="#D1D5DB" fontSize="12">"생산성 올랐다!"</text>

              {/* Arrow pointing to revenue */}
              <text x="125" y="270" textAnchor="middle" fill="#6B7280" fontSize="11">하지만...</text>
            </g>

            {/* Arrow */}
            <g transform="translate(340, 240)">
              <path d="M0 0 L30 0 L24 -6 M30 0 L24 6" fill="none" stroke="#4B5563" strokeWidth="2" />
            </g>

            {/* Right Side - Revenue unchanged */}
            <g transform="translate(390, 110)">
              <rect x="0" y="0" width="250" height="280" rx="12" fill="#1E293B" stroke="#334155" strokeWidth="1" />
              <text x="125" y="32" textAnchor="middle" fill="#EF4444" fontSize="15" fontWeight="bold">📊 매출 변화</text>

              {/* Revenue bars - flat */}
              {["Q1", "Q2", "Q3", "Q4"].map((q, i) => (
                <g key={q}>
                  <rect x={30 + i * 52} y={100} width={36} height={60} rx="4" fill="#374151" />
                  <rect x={30 + i * 52} y={100} width={36} height={60} rx="4" fill={i >= 2 ? "#4B5563" : "#4B5563"} opacity={0.8} />
                  <text x={48 + i * 52} y={175} textAnchor="middle" fill="#9CA3AF" fontSize="10">{q}</text>
                </g>
              ))}

              {/* Flat line */}
              <line x1="30" y1="100" x2="240" y2="100" stroke="#EF4444" strokeWidth="1.5" strokeDasharray="4,4" />
              <text x="240" y="95" textAnchor="end" fill="#EF4444" fontSize="10">Max 천장</text>

              {/* Bear confused */}
              <BearFace x={125} y={225} size={60} expression="confused" />
              <text x="125" y="270" textAnchor="middle" fill="#D1D5DB" fontSize="12">"...그래서 뭐가 바뀌었지?"</text>
            </g>

            {/* Bottom message */}
            <rect x="120" y="410" width="480" height="50" rx="25" fill="#F59E0B" fillOpacity="0.1" stroke="#F59E0B" strokeWidth="1" />
            <text x="360" y="440" textAnchor="middle" fill="#F59E0B" fontSize="15" fontWeight="bold">빠른 개인 ≠ 성장하는 조직</text>
          </svg>
        </div>
      )}

      {/* Min/Max Diagram */}
      {activeView === "minmax" && (
        <div className="bg-gray-900 rounded-2xl p-1" style={{ width: 720, height: 480 }}>
          <svg viewBox="0 0 720 480" xmlns="http://www.w3.org/2000/svg">
            <defs>
              <linearGradient id="bg2" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#1a1a2e" />
                <stop offset="100%" stopColor="#0f0f1a" />
              </linearGradient>
            </defs>
            <rect width="720" height="480" fill="url(#bg2)" rx="16" />

            {/* Title */}
            <text x="360" y="45" textAnchor="middle" fill="#F5F5F5" fontSize="24" fontWeight="bold">사업의 Min과 Max</text>

            {/* Building metaphor */}
            {/* Max ceiling */}
            <line x1="100" y1="100" x2="620" y2="100" stroke="#F59E0B" strokeWidth="2" strokeDasharray="8,4" />
            <text x="630" y="105" fill="#F59E0B" fontSize="13" fontWeight="bold">Max</text>
            <text x="630" y="120" fill="#9CA3AF" fontSize="10">매출 상방</text>

            {/* The gap area */}
            <rect x="100" y="100" width="520" height="240" fill="#F59E0B" fillOpacity="0.03" />
            <text x="360" y="215" textAnchor="middle" fill="#4B5563" fontSize="16">사업 성장 영역</text>
            <text x="360" y="238" textAnchor="middle" fill="#4B5563" fontSize="12">사업/세일즈/마케팅이 여는 공간</text>

            {/* Min floor */}
            <line x1="100" y1="340" x2="620" y2="340" stroke="#3B82F6" strokeWidth="2" />
            <text x="630" y="345" fill="#3B82F6" fontSize="13" fontWeight="bold">Min</text>
            <text x="630" y="360" fill="#9CA3AF" fontSize="10">기술 바닥</text>

            {/* Min zone */}
            <rect x="100" y="340" width="520" height="70" fill="#3B82F6" fillOpacity="0.08" />

            {/* Bear on Min floor */}
            <BearFace x={200} y={370} size={50} expression="happy" />
            <text x="200" y="400" textAnchor="middle" fill="#93C5FD" fontSize="10">장애 방지</text>
            <BearFace x={300} y={370} size={50} expression="neutral" />
            <text x="300" y="400" textAnchor="middle" fill="#93C5FD" fontSize="10">성능 개선</text>
            <BearFace x={400} y={370} size={50} expression="happy" />
            <text x="400" y="400" textAnchor="middle" fill="#93C5FD" fontSize="10">버그 수정</text>
            <BearFace x={500} y={370} size={50} expression="sweat" />
            <text x="500" y="400" textAnchor="middle" fill="#93C5FD" fontSize="10">AI로 2x</text>

            {/* Arrow showing AI effect */}
            <g>
              <path d="M540 360 L540 330" fill="none" stroke="#F59E0B" strokeWidth="2" />
              <polygon points="540,325 535,333 545,333" fill="#F59E0B" />
              <text x="575" y="340" fill="#F59E0B" fontSize="10">생산성 ↑</text>
            </g>

            {/* But Max stays same */}
            <g>
              <text x="360" y="140" textAnchor="middle" fill="#6B7280" fontSize="13">↑ Max는 그대로</text>
              <text x="360" y="158" textAnchor="middle" fill="#6B7280" fontSize="11">개발 생산성으로는 이 천장이 안 올라감</text>
            </g>

            {/* Bottom */}
            <rect x="100" y="430" width="520" height="36" rx="18" fill="#1E293B" stroke="#334155" strokeWidth="1" />
            <text x="360" y="453" textAnchor="middle" fill="#D1D5DB" fontSize="13">Min을 효율적으로 달성 = 영업이익 최적화 | Max를 높이기 = 매출 성장</text>
          </svg>
        </div>
      )}

      {/* Core Question */}
      {activeView === "question" && (
        <div className="bg-gray-900 rounded-2xl p-1" style={{ width: 720, height: 480 }}>
          <svg viewBox="0 0 720 480" xmlns="http://www.w3.org/2000/svg">
            <defs>
              <linearGradient id="bg3" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor="#1a1a2e" />
                <stop offset="100%" stopColor="#0f0f1a" />
              </linearGradient>
            </defs>
            <rect width="720" height="480" fill="url(#bg3)" rx="16" />

            {/* Old Question - crossed out */}
            <g transform="translate(360, 100)">
              <rect x="-260" y="-35" width="520" height="70" rx="12" fill="#1E293B" stroke="#374151" strokeWidth="1" />
              <text x="0" y="-5" textAnchor="middle" fill="#6B7280" fontSize="14">CTO 모임의 질문</text>
              <text x="0" y="20" textAnchor="middle" fill="#9CA3AF" fontSize="16">"개발자의 AI 생산성 향상이 느껴지시나요?"</text>
              <line x1="-200" y1="17" x2="200" y2="17" stroke="#EF4444" strokeWidth="2" opacity="0.6" />
            </g>

            {/* Arrow down */}
            <path d="M360 175 L360 205" fill="none" stroke="#4B5563" strokeWidth="2" />
            <polygon points="360,212 355,202 365,202" fill="#4B5563" />
            <text x="360" y="230" textAnchor="middle" fill="#6B7280" fontSize="12">바꿔야 할 질문</text>

            {/* New Question */}
            <g transform="translate(360, 275)">
              <rect x="-280" y="-40" width="560" height="80" rx="16" fill="#F59E0B" fillOpacity="0.1" stroke="#F59E0B" strokeWidth="2" />
              <text x="0" y="8" textAnchor="middle" fill="#F59E0B" fontSize="20" fontWeight="bold">"AI로 이전에는 불가능했던 사업 기회가 열렸는가?"</text>
            </g>

            {/* Bear at bottom */}
            <BearFace x={360} y={390} size={65} expression="neutral" />

            {/* Final punch line */}
            <rect x="140" y="430" width="440" height="36" rx="18" fill="#1E293B" stroke="#F59E0B" strokeWidth="1" />
            <text x="360" y="453" textAnchor="middle" fill="#F5F5F5" fontSize="14" fontWeight="bold">"아낀 시간으로 그 다음에는 무엇을 했나요?"</text>
          </svg>
        </div>
      )}

      <p className="text-gray-500 text-xs mt-4">클릭하여 각 컨셉 이미지를 확인하세요</p>
    </div>
  );
}
